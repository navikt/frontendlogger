package no.nav;

import no.nav.sbl.util.EnvironmentUtils;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static no.nav.metrics.MetricsFactory.createEvent;

@Component
@Path("/event")
public class MetrikkRessurs {

    @POST
    public void lagEvent(Event event) {
        no.nav.metrics.Event newEvent = createEvent(event.getName());

        event.getFields().entrySet().forEach(entry -> newEvent.addFieldToReport(entry.getKey(), entry.getValue()));
        event.getTags().entrySet().forEach(entry -> newEvent.addTagToReport(entry.getKey(), entry.getValue()));

        /*
         Legg på namespace og cluster for å gjøre migreringen til versjon 2 av metrics modulen litt enklere.
         Versjon 2 legger på dette automatisk, så koden under kan fjernes når vi oppgraderer frontendloggeren.
        */
        event.getTags().putIfAbsent("namespace", EnvironmentUtils.getNamespace().orElse("NO_NAMESPACE"));
        event.getTags().putIfAbsent("cluster", EnvironmentUtils.getClusterName().orElse("NO_CLUSTER"));

        newEvent.report();
    }
}
