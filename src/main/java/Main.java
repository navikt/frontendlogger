
import no.nav.ApplicationConfig;
import no.nav.apiapp.ApiApp;

public class Main {

    public static void main(String... args) throws Exception {
        ApiApp.startApp(ApplicationConfig.class, args);
    }

}