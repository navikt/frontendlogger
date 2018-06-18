package no.nav;

import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static no.nav.metrics.MetricsFactory.createEvent;

@Component
@Path("/event")
public class MetrikkRessurs {

    @POST
    public void lagEvent(Metrikk metrikk) {
        createEvent(metrikk.getEvent())
                .addFieldToReport(metrikk.getField(), metrikk.getValue())
                .report();
    }
}
