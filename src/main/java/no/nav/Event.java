package no.nav;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
class Event {
    private String name;
    private Map<String, Object> fields;
    private Map<String, String> tags;

    @JsonIgnore
    private String url;

    @JsonIgnore
    private String userAgent;

    @JsonIgnore
    private String appname;
}
