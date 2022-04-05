package no.nav.frontendlogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import static no.nav.common.utils.EnvironmentUtils.NAIS_APP_NAME_PROPERTY_NAME;

@EnableAutoConfiguration
@Import(ApplicationTestConfig.class)
public class MainTest {

    public static void main(String... args) {
        System.setProperty(NAIS_APP_NAME_PROPERTY_NAME, "frontendlogger");
        System.setProperty("PINPOINT_BASE_URL", "http://localhost:8991");

        SpringApplication application = new SpringApplication(MainTest.class);
        application.setAdditionalProfiles("local");
        application.run(args);
    }

}
