package no.nav.frontendlogger;

import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestChecks;
import no.nav.common.health.selftest.SelfTestMeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class HealthConfig {

    @Bean
    public SelfTestChecks selfTestChecks(PinpointClient pinpointClient) {
        return new SelfTestChecks(new ArrayList<>(Arrays.asList(
                new SelfTestCheck("pinpoint", false, pinpointClient)
        )));
    }

    @Bean
    public SelfTestMeterBinder selfTestMeterBinder(SelfTestChecks selfTestChecks) {
        return new SelfTestMeterBinder(selfTestChecks);
    }

}
