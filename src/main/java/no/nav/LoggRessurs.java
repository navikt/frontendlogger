package no.nav;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.metrics.MetricsFactory;
import no.nav.sbl.rest.RestUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Optional.ofNullable;
import static javax.ws.rs.client.Entity.json;
import static net.logstash.logback.marker.Markers.appendEntries;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Path("/{level}")
public class LoggRessurs {

    private static final Logger LOG = getLogger(LoggRessurs.class);
    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();
    private static final String APPLICATION_ATTRIBUTE_NAME = "appname";

    private static final Map<Level, BiConsumer<Marker, String>> logMap;

    private static final Client client = RestUtils.createClient();

    static {
        logMap = new HashMap<>();
        logMap.put(Level.TRACE, LOG::trace);
        logMap.put(Level.DEBUG, LOG::debug);
        logMap.put(Level.INFO, LOG::info);
        logMap.put(Level.WARN, LOG::warn);
        logMap.put(Level.ERROR, LOG::error);
    }

    private final String pinpointApiUrl = getOptionalProperty("PINPOINT_BASE_URL").orElse("https://pinpoint.default.svc.nais.local") + "/pinpoint/api/pinpoint";

    @POST
    public void log(@PathParam("level") String level, Map<String, Object> logMsg) {
        Level logLevel = Level.valueOf(level.toUpperCase());
        String appname = ofNullable(logMsg.get(APPLICATION_ATTRIBUTE_NAME)).map(Object::toString).orElse("unknown");

        meterRegistry.counter(
                "frontend_logger",
                "origin_app",
                appname,
                "level",
                logLevel.name()
        ).increment();

        logToLogback(logLevel, logMsg);
        logToPinpoint(logLevel, logMsg);
    }

    private static void logToLogback(Level logLevel, Map<String, Object> logMsg) {
        logMap.get(logLevel).accept(appendEntries(logMsg), null);
    }

    private void logToPinpoint(Level logLevel, Map<String, Object> logMsg) {
        Object pinpoint = logMsg.get("pinpoint");
        if (pinpoint != null) {
            logMsg.remove("pinpoint");
            client.target(pinpointApiUrl)
                    .request()
                    .async()
                    .post(json(pinpoint), new PinpointCallback(logLevel));
        }
    }

    private static class PinpointCallback implements InvocationCallback<Map<String, Object>> {
        private final Level logLevel;

        private PinpointCallback(Level logLevel) {
            this.logLevel = logLevel;
        }

        @Override
        public void completed(Map<String, Object> map) {
            logToLogback(logLevel, map);
        }

        @Override
        public void failed(Throwable throwable) {
            LOG.error(throwable.getMessage(), throwable);
        }

    }
}