package no.nav;


import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MetrikkRessurs.class,
        LoggRessurs.class,
        PinpointClient.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public String getContextPath() {
        return "/frontendlogger";
    }

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }

}