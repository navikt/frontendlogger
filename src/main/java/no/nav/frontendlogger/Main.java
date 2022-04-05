package no.nav.frontendlogger;

import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String... args) throws Exception {
        SslUtils.setupTruststore();
//        setupCors();
        SpringApplication.run(Main.class, args);
    }
/*
    private static void setupCors() {
        setProperty(CORS_ALLOWED_HEADERS, ALLOWED_HEADERS, EnvironmentUtils.Type.PUBLIC);
        getOptionalProperty("CORS_ALLOWED_ORIGINS")
                .ifPresent((value) -> setProperty(CORS_ALLOWED_ORIGINS, value, EnvironmentUtils.Type.PUBLIC));
    }

    private static final String ALLOWED_HEADERS = String.join(",",
            "Accept",
            "Accept-language",
            "Content-Language",
            "Content-Type",
            "Nav-Consumer-Id"
    );*/
}
