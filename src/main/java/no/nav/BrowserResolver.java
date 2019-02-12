package no.nav;

import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class BrowserResolver {

    private static final Map<String, String> PATTERNS = initPatterns();

    private static Map<String, String> initPatterns() {
        val map = new HashMap<String, String>();

        map.put("chrome", "^((?!Chromium).)*(Chrome|CriOS)\\/((?!OPR|Edge).)*$");
        map.put("chromium", "^.*Chromium/.*Chrome/.*$");
        map.put("safari", "^((?!Chromium|Chrome|CriOS|Firefox|FxiOS|OPR).)*Safari\\/.*$");
        map.put("firefox", "^.*(Firefox|FxiOS)\\/.*$");
        map.put("opera", "^.*OPR\\/.*$");
        map.put("ie", "^.*Trident\\/.*$");
        map.put("edge", "^.*Edge\\/.*$");
        map.put("crawler", "^Googlebot\\/.*$");

        return map;
    }


    public static String resolveBrowser(Map<String, Object> logMsg) {
        return ofNullable(logMsg.get("userAgent"))
                .map(Object::toString)
                .flatMap(userAgent -> {
                    val matches = matchBrowsers(userAgent);
                    if(matches.size() == 1)
                        return Optional.of(matches.get(0));
                    else
                        return Optional.empty();
                })
                .orElse("unknown");
    }

    private static List<String> matchBrowsers(String userAgent) {
        return PATTERNS
                .entrySet()
                .stream()
                .filter(x -> userAgent.matches(x.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
