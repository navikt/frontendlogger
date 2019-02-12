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
import static no.nav.BrowserResolver.resolveBrowser;

@Component
@Path(YtelseRessurs.PATH)
@Slf4j
public class YtelseRessurs {

    public static final String PATH = "/performance";
    public static final String PAGE_LOAD_TIME_ATTRIBUTE = "pageLoadTime";

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();

    @POST
    public void registrerSidelast(Map<String, Object> logMsg) {
        Number pageLoadTime = (Number) logMsg.get(PAGE_LOAD_TIME_ATTRIBUTE);



        meterRegistry.timer("page_load_time",
                "origin_app", resolveApplicationName(logMsg),
                "browser", resolveBrowser(logMsg)
        ).record(pageLoadTime.longValue(), TimeUnit.MILLISECONDS);

        log.info(Markers.appendEntries(logMsg), null);
    }
}
