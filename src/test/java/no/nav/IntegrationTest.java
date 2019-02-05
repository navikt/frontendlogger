package no.nav;

import no.nav.apiapp.ApiApp;
import no.nav.testconfig.ApiAppTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.util.SocketUtils;

import java.net.URL;
import java.util.List;

public class IntegrationTest {

    private static ApiApp apiApp;

    @BeforeClass
    public static void startServer() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName("frontendlogger")
                .build()
        );
        apiApp = ApiApp.startApiApp(ApplicationConfig.class, new String[]{"" + SocketUtils.findAvailableTcpPort()});
    }

    @AfterClass
    public static void stopServer() {
        if (apiApp != null) {
            apiApp.getJetty().stop.run();
        }
    }

    protected String getBaseUrl() {
        List<URL> baseUrls = apiApp.getJetty().getBaseUrls();
        return baseUrls.get(0).toString() + "/api";
    }

}
