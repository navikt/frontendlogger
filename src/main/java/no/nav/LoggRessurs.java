package no.nav;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("/")
public class LoggRessurs {

    @GET
    public String get() {
        return "alt ok!";
    }
}