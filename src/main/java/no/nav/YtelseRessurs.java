package no.nav;

import io.micrometer.core.instrument.DistributionSummary;
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
    private static final DistributionSummary summary =
            DistributionSummary
                    .builder("page_load_time")
                    .publishPercentileHistogram()
                    .register(meterRegistry);

    private static final Timer timer =
            Timer
                    .builder("page_load_time_2")
                    .publishPercentileHistogram()
                    .register(meterRegistry);

    @POST
    public void lagEvent(Map<String, Object> logMsg) {

        summary.record(Math.random() * 100);
        timer.record(Math.round(Math.random() * 100), TimeUnit.MILLISECONDS);
    }
}
