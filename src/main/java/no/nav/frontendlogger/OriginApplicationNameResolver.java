package no.nav.frontendlogger;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static no.nav.common.utils.EnvironmentUtils.getOptionalProperty;

public class OriginApplicationNameResolver {


    public static String resolveApplicationName(Map<String, Object> logMsg) {
        return ofNullable(logMsg.get("appname"))
                .map(Object::toString)
                .map(String::toLowerCase)
                .filter(OriginApplicationNameResolver::wellKnownApp)
                .orElse("unknown");
    }

    static boolean wellKnownApp(String appname) {
        String applicationEnvironmentVariableName = appname.toUpperCase().replaceAll("-", "_") + "_SERVICE_HOST";
        return getOptionalProperty(applicationEnvironmentVariableName).isPresent();
    }

}
