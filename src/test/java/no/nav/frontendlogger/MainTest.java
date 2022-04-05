package no.nav.frontendlogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static no.nav.common.utils.EnvironmentUtils.NAIS_APP_NAME_PROPERTY_NAME;

@SpringBootApplication
public class MainTest {

    public static void main(String... args) {
        System.setProperty(NAIS_APP_NAME_PROPERTY_NAME, "frontendlogger");
        SpringApplication.run(MainTest.class, args);
    }

}
