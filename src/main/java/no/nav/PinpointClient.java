package no.nav;

import lombok.extern.slf4j.Slf4j;
import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.apiapp.util.UrlUtils;
import no.nav.sbl.rest.RestUtils;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import java.util.Map;
import java.util.function.Consumer;

import static javax.ws.rs.client.Entity.json;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;
import static no.nav.sbl.util.EnvironmentUtils.requireApplicationName;

@Component
@Slf4j
public class PinpointClient implements Helsesjekk {


    private final Client client = RestUtils.createClient();

    private final String pinpointApplicationName = requireApplicationName().contains("-fss") ? "pinpoint-fss" : "pinpoint";
    private final String pinpointApiUrl = getOptionalProperty("PINPOINT_BASE_URL").orElseGet(() -> UrlUtils.clusterUrlForApplication(pinpointApplicationName)) + "/pinpoint/api";
    private final HelsesjekkMetadata helsesjekkMetadata = new HelsesjekkMetadata(
            "pinpoint",
            pinpointApiUrl,
            "pinpoint",
            false
    );

    public void enrichErrorData(Object errorData, Consumer<Map<String, Object>> callback) {
        client.target(pinpointApiUrl)
                .path("pinpoint")
                .request()
                .async()
                .post(json(errorData), new PinpointCallback(callback));
    }

    @Override
    public void helsesjekk() {
        if (client.target(pinpointApiUrl).path("ping").request().get().getStatus() != 200) {
            throw new IllegalStateException();
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return helsesjekkMetadata;
    }

    private static class PinpointCallback implements InvocationCallback<Map<String, Object>> {
        private final Consumer<Map<String, Object>> callback;

        private PinpointCallback(Consumer<Map<String, Object>> callback) {
            this.callback = callback;
        }

        @Override
        public void completed(Map<String, Object> map) {
            callback.accept(map);
        }

        @Override
        public void failed(Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }

    }
}
