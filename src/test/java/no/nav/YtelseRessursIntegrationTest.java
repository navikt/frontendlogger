package no.nav;


import no.nav.sbl.rest.RestUtils;
import org.junit.Test;

import java.util.HashMap;

import static javax.ws.rs.client.Entity.json;
import static no.nav.YtelseRessurs.PAGE_LOAD_TIME_ATTRIBUTE;
import static no.nav.YtelseRessurs.PATH;
import static org.assertj.core.api.Assertions.assertThat;

public class YtelseRessursIntegrationTest extends IntegrationTest {

    @Test
    public void smoketest() {
        pageLoadTime(42);
        pageLoadTime(666.666);
    }

    private void pageLoadTime(Number value) {
        HashMap<String, Object> performanceData = new HashMap<>();
        performanceData.put(PAGE_LOAD_TIME_ATTRIBUTE, value);

        int httpStatus = RestUtils.withClient(c -> c.target(getBaseUrl())
                .path(PATH)
                .request()
                .post(json(performanceData))
                .getStatus()
        );

        assertThat(httpStatus).isEqualTo(204);
    }

}
