package no.nav;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import no.nav.metrics.MetricsFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Path("/performance")
public class YtelseRessurs {

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();

    private static final Timer timer =
            Timer
                    .builder("page_load_time")
                    .publishPercentileHistogram()
                    .register(meterRegistry);

    @POST
    public void registrerSidelast(Map<String, Object> logMsg) {
        long pageLoadTime = Math.round((double)logMsg.get("pageLoadTime"));
        timer.record(pageLoadTime, TimeUnit.MILLISECONDS);
    }
}
