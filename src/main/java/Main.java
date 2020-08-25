
import no.nav.ApplicationConfig;
import no.nav.apiapp.ApiApp;

import java.util.Optional;

import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;

public class Main {

    public static void main(String... args) throws Exception {
        Optional<String> optionalProperty = getOptionalProperty("cors.allowed.origins");
        System.out.println("Found CORS " + optionalProperty.isPresent());
        System.out.println("Found CORS " + optionalProperty.orElse("N/A"));

        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
