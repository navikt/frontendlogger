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
    public boolean brukSTSHelsesjekk() {
        return false;
    }

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }

}