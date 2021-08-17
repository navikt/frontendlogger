import no.nav.ApplicationConfig;
import no.nav.apiapp.ApiApp;
import no.nav.sbl.util.EnvironmentUtils;

import static no.nav.apiapp.rest.NavCorsFilter.CORS_ALLOWED_HEADERS;
import static no.nav.apiapp.rest.NavCorsFilter.CORS_ALLOWED_ORIGINS;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;
import static no.nav.sbl.util.EnvironmentUtils.setProperty;

public class Main {
    public static void main(String... args) throws Exception {
        setupCors();
        ApiApp.runApp(ApplicationConfig.class, args);
    }

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
    );
}
