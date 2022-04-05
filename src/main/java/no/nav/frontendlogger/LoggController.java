package no.nav.frontendlogger;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.logstash.logback.marker.Markers.appendEntries;
import static no.nav.frontendlogger.OriginApplicationNameResolver.resolveApplicationName;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api")
public class LoggController {

    private static final Logger LOG = getLogger(LoggController.class);

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
    private final MeterRegistry meterRegistry;

    @Autowired
    public LoggController(PinpointClient pinpointClient,
                          MeterRegistry meterRegistry) {
        this.pinpointClient = pinpointClient;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/info")
    public void info(@RequestBody Map<String, Object> logMsg) {
        log(Level.INFO, logMsg);
    }

    @PostMapping("/warn")
    public void warn(@RequestBody Map<String, Object> logMsg) {
        log(Level.WARN, logMsg);
    }

    @PostMapping("/error")
    public void error(@RequestBody Map<String, Object> logMsg) {
        log(Level.ERROR, logMsg);
    }

    @PostMapping("/debug")
    public void debug(@RequestBody Map<String, Object> logMsg) {
        log(Level.DEBUG, logMsg);
    }

    @PostMapping("/trace")
    public void trace(@RequestBody Map<String, Object> logMsg) {
        log(Level.TRACE, logMsg);
    }

    public void log(Level level, Map<String, Object> logMsg) {
        String appname = resolveApplicationName(logMsg);

        meterRegistry.counter(
                "frontend_logger",
                "origin_app",
                appname,
                "level",
                level.name()
        ).increment();

        Object pinpoint = logMsg.get("pinpoint");

        if (pinpoint == null) {
            logToLogback(level, logMsg);
        } else {
            logMsg.remove("pinpoint");
            pinpointClient.enrichErrorData(appname, pinpoint, (enrichedError) -> {
                logMsg.putAll(enrichedError);
                logToLogback(level, logMsg);
            });
        }
    }

    private static void logToLogback(Level logLevel, Map<String, Object> logMsg) {
        logMap.get(logLevel).accept(appendEntries(logMsg), null);
    }

}
