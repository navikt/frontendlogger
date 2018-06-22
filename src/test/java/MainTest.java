import no.nav.testconfig.ApiAppTest;

public class MainTest {

    public static void main(String... args) throws Exception {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName("frontendlogger")
                .build()
        );
        Main.main(args);
    }

}