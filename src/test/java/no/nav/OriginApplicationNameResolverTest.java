package no.nav;


import no.nav.sbl.dialogarena.test.junit.SystemPropertiesRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OriginApplicationNameResolverTest {


    @Rule
    public SystemPropertiesRule systemPropertiesRule = new SystemPropertiesRule();

    @Test
    public void unknownApp__false() {
        assertThat(OriginApplicationNameResolver.wellKnownApp("evil-app")).isFalse();
    }

    @Test
    public void knownApp__true() {
        systemPropertiesRule.setProperty("KNOWN_APP_SERVICE_HOST", "127.0.0.1");
        assertThat(OriginApplicationNameResolver.wellKnownApp("known-app")).isTrue();
    }

}