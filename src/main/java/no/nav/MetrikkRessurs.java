package no.nav;

import lombok.experimental.var;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static no.nav.metrics.MetricsFactory.createEvent;

@Component
@Path("/event")
public class MetrikkRessurs {

    @POST
    public void lagEvent(Event event) {
        var newEvent = createEvent(event.getName());
        event.getFields().entrySet().forEach(entry -> newEvent.addFieldToReport(entry.getKey(), entry.getValue()));
        event.getTags().entrySet().forEach(entry -> newEvent.addTagToReport(entry.getKey(), entry.getValue()));

    }
}
