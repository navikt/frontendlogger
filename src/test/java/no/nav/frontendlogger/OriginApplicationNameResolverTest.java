package no.nav.frontendlogger;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OriginApplicationNameResolverTest {

    @Test
    public void unknownApp__false() {
        assertFalse(OriginApplicationNameResolver.wellKnownApp("evil-app"));
    }

    @Test
    public void knownApp__true() {
        System.setProperty("KNOWN_APP_SERVICE_HOST", "127.0.0.1");
        assertTrue(OriginApplicationNameResolver.wellKnownApp("known-app"));
    }
}
