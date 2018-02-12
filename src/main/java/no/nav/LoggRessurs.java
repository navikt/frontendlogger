package no.nav;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.logstash.logback.marker.Markers.appendEntries;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Path("/{level}")
public class LoggRessurs {

    private static final Logger LOG = getLogger(LoggRessurs.class);

    private static final Map<Level, BiConsumer<Marker, String>> logMap;

    static {
        logMap = new HashMap<>();
        logMap.put(Level.TRACE, LOG::trace);
        logMap.put(Level.DEBUG, LOG::debug);
        logMap.put(Level.INFO, LOG::info);
        logMap.put(Level.WARN, LOG::warn);
        logMap.put(Level.ERROR, LOG::error);
    }

    @POST
    public void log(@PathParam("level") String level, Map<String, Object> logMsg) {
        Level logLevel = Level.valueOf(level.toUpperCase());
        logMap.get(logLevel).accept(appendEntries(logMsg), null);
    }
}