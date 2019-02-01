package no.nav;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import no.nav.metrics.MetricsFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static no.nav.OriginApplicationNameResolver.resolveApplicationName;

@Component
@Path("/performance")
@Slf4j
public class YtelseRessurs {

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();

    @POST
    public void registrerSidelast(Map<String, Object> logMsg) {
        long pageLoadTime = Math.round((double) logMsg.get("pageLoadTime"));

        meterRegistry.timer("page_load_time",
                "origin_app",
                resolveApplicationName(logMsg)
        ).record(pageLoadTime, TimeUnit.MILLISECONDS);

        log.info(Markers.appendEntries(logMsg), null);
    }
}
