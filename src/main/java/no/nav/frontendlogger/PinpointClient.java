package no.nav.frontendlogger;

import com.fasterxml.jackson.databind.type.MapType;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.json.JsonUtils;
import no.nav.common.rest.client.RestClient;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import static no.nav.common.rest.client.RestUtils.toJsonRequestBody;
import static no.nav.common.utils.EnvironmentUtils.getOptionalProperty;
import static no.nav.common.utils.EnvironmentUtils.requireApplicationName;
import static no.nav.common.utils.UrlUtils.createServiceUrl;

@Slf4j
public class PinpointClient implements HealthCheck {

    private final OkHttpClient client;

    public PinpointClient() {
        client = RestClient.baseClient();
    }

    private final String pinpointApplicationName = requireApplicationName().contains("-fss") ? "pinpoint-fss" : "pinpoint";
    private final String pinpointApiUrl = getOptionalProperty("PINPOINT_BASE_URL").orElseGet(() -> createServiceUrl(pinpointApplicationName)) + "/pinpoint/api";
    private final String pinpointIsAliveUrl = getOptionalProperty("PINPOINT_BASE_URL").orElseGet(() -> createServiceUrl(pinpointApplicationName)) + "/pinpoint/internal/isAlive";

    public void enrichErrorData(String appname, Object errorData, Consumer<Map<String, Object>> callback) {
        Request request = new Request.Builder()
                .url(pinpointApiUrl + "/pinpoint?appname=" + appname)
                .post(toJsonRequestBody(errorData))
                .build();

        client.newCall(request).enqueue(new PinpointCallback(callback));
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(pinpointIsAliveUrl, client);
    }

    private static class PinpointCallback implements Callback {

        private final Consumer<Map<String, Object>> callback;

        private PinpointCallback(Consumer<Map<String, Object>> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            log.error(e.getMessage(), e);
        }

        @Override
        public void onResponse(Call call, Response response) {

            MapType mapType = JsonUtils.getMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class);

            try (ResponseBody responseBody = response.body()) {
                try {
                    Map<String, Object> value = JsonUtils.getMapper().readValue(responseBody.string(), mapType);
                    callback.accept(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
