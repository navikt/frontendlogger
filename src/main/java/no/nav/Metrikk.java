package no.nav;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class Metrikk {
    private String event;
    private String field;
    private Object value;
}
