package no.nav;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.metrics.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Optional.ofNullable;
import static net.logstash.logback.marker.Markers.appendEntries;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Path("/{level}")
public class LoggRessurs {

    private static final Logger LOG = getLogger(LoggRessurs.class);
    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();
    private static final String APPLICATION_ATTRIBUTE_NAME = "appname";

    private static final Map<Level, BiConsumer<Marker, String>> logMap;


    static {
        logMap = new HashMap<>();
        logMap.put(Level.TRACE, LOG::trace);
        logMap.put(Level.DEBUG, LOG::debug);
        logMap.put(Level.INFO, LOG::info);
        logMap.put(Level.WARN, LOG::warn);
        logMap.put(Level.ERROR, LOG::error);
    }


    private final PinpointClient pinpointClient;

    @Inject
    public LoggRessurs(PinpointClient pinpointClient) {
        this.pinpointClient = pinpointClient;
    }

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

        Object pinpoint = logMsg.get("pinpoint");
        if (pinpoint != null) {
            logMsg.remove("pinpoint");
        }
        
        logToLogback(logLevel, logMsg);

        if (pinpoint != null) {
            pinpointClient.enrichErrorData(pinpoint, (enrichedError) -> logToLogback(logLevel, enrichedError));
        }
    }

    private static void logToLogback(Level logLevel, Map<String, Object> logMsg) {
        logMap.get(logLevel).accept(appendEntries(logMsg), null);
    }

}