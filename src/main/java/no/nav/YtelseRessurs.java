    package no.nav;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import no.nav.metrics.MetricsFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.util.Map;

@Component
@Path("/performance")
public class YtelseRessurs {

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();
    private static final DistributionSummary summary =
            DistributionSummary
                    .builder("page_load_time")
                    .sla(50, 70, 90, 95)
                    .register(meterRegistry);

    @POST
    public void lagEvent(Map<String, Object> logMsg) {
         summary.record(Math.random() * 100);
    }
}
