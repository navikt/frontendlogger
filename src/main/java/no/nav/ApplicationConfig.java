package no.nav;


import no.nav.apiapp.ApiApplication.NaisApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MetrikkRessurs.class,
        LoggRessurs.class
})
public class ApplicationConfig implements NaisApiApplication {

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public boolean brukSTSHelsesjekk() {
        return false;
    }

    public static final String APPLICATION_NAME = "frontendlogger";

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }
}