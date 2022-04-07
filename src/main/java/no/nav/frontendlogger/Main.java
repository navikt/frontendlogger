package no.nav.frontendlogger;

import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String... args) {
        SslUtils.setupTruststore();
        SpringApplication.run(Main.class, args);
    }
}
