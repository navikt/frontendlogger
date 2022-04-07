package no.nav.frontendlogger;

import no.nav.common.metrics.MetricsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import java.util.Optional;

import static no.nav.common.utils.EnvironmentUtils.isProduction;

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

        // Legger til environment for bakoverkompabilitet for metrikker som filtrerer på dette
        resolveEnvironmentTag().ifPresent(environment ->
                newEvent.getTags().putIfAbsent("environment", environment)
        );

        metricsClient.report(newEvent);
    }

    private Optional<String> resolveEnvironmentTag() {
        if (isProduction().orElse(false)) {
            return Optional.of("p");
        }

        String hostName = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                .map(requestAttributes -> (ServletRequestAttributes) requestAttributes)
                .map(ServletRequestAttributes::getRequest)
                .map(ServletRequest::getServerName)
                .orElse("");

        if (hostName.contains("q1")) {
            return Optional.of("q1");
        }

        if (hostName.contains("q0")) {
            return Optional.of("q0");
        }

        return Optional.empty();
    }
}
