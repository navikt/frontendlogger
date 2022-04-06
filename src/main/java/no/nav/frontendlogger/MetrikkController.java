package no.nav.frontendlogger;

import no.nav.common.metrics.MetricsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.common.utils.EnvironmentUtils.isProduction;
import static no.nav.common.utils.EnvironmentUtils.resolveHostName;

@RestController
@RequestMapping("/api/event")
public class MetrikkController {

    private final MetricsClient metricsClient;

    @Autowired
    public MetrikkController(MetricsClient metricsClient) {
        this.metricsClient = metricsClient;
    }

    @PostMapping
    public void lagEvent(@RequestBody Event event) {
        no.nav.common.metrics.Event newEvent = new no.nav.common.metrics.Event(event.getName() + ".event");

        event.getFields().forEach(newEvent::addFieldToReport);
        event.getTags().forEach(newEvent::addTagToReport);

        // Legger til environment for bakoverkompabilitet
        newEvent.getTags().putIfAbsent("environment", resolveEnvironmentTag());

        metricsClient.report(newEvent);
    }

    private String resolveEnvironmentTag() {
        if (isProduction().orElse(false)) {
            return "p";
        }

        String hostName = resolveHostName();

        if (hostName.contains("q1")) {
            return "q1";
        }

        if (hostName.contains("q0")) {
            return "q0";
        }

        return "q";
    }
}
