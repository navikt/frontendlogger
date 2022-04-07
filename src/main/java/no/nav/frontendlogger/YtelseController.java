package no.nav.frontendlogger;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static no.nav.frontendlogger.OriginApplicationNameResolver.resolveApplicationName;
import static no.nav.frontendlogger.BrowserResolver.resolveBrowser;

@RestController
@RequestMapping("/api/performance")
@Slf4j
public class YtelseController {

    public static final String PAGE_LOAD_TIME_ATTRIBUTE = "pageLoadTime";

    private final MeterRegistry meterRegistry;

    @Autowired
    public YtelseController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public void registrerSidelast(@RequestBody Map<String, Object> logMsg) {
        Number pageLoadTime = (Number) logMsg.get(PAGE_LOAD_TIME_ATTRIBUTE);

        meterRegistry.timer("page_load_time",
                "origin_app", resolveApplicationName(logMsg),
                "browser", resolveBrowser(logMsg)
        ).record(pageLoadTime.longValue(), TimeUnit.MILLISECONDS);

        log.info(Markers.appendEntries(logMsg), null);
    }
}
