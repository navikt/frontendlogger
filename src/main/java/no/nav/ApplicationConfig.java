package no.nav;


import no.nav.apiapp.ApiApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@Import({
        ScriptRessurs.class,
        LoggRessurs.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public Sone getSone() {
        return FSS;
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public boolean brukSTSHelsesjekk() {
        return false;
    }

    public static final String APPLICATION_NAME = "frontendlogger";

}