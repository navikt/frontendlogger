package no.nav;

import lombok.Builder;
import lombok.Value;

import java.util.Map;


@Value
@Builder
class Event {
    private String name;
    private Map<String,Object> fields;
    private Map<String,String> tags;
}
