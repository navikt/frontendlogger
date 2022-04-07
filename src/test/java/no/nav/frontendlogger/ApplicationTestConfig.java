package no.nav.frontendlogger;

import no.nav.common.metrics.MetricsClient;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Profile("local")
@Configuration
@Import({
        MetrikkController.class,
        LoggController.class,
        YtelseController.class,
        HealthConfig.class
})
public class ApplicationTestConfig extends ApplicationConfig {

    @Bean
    @Override
    public MetricsClient metricsClient() {
        return mock(MetricsClient.class);
    }
}
