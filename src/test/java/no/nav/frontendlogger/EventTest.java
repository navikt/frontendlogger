package no.nav.frontendlogger;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.frontendlogger.Event;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class EventTest {



    @Test
    public void test() throws IOException {

        String json = "{\"name\":\"Tiltakinfo-sidevisning\",\"fields\":{\"underOppfolging\":true},\"tags\":{},\"url\":\"https://test\",\"userAgent\":\"testvalue\",\"appname\":\"tiltakinfo\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        Event event = objectMapper.readValue(json, Event.class);

        assertEquals(event.getName(), "Tiltakinfo-sidevisning");
        assertEquals(event.getFields().get("underOppfolging"), true);

    }

    @Test
    public void testSerialize() throws JsonProcessingException {

        Event event = new Event();
        event.setName("name");
        event.setFields(new HashMap<String, Object>() {{
            put("field1", "1");
            put("field", true);
        }});
        event.setTags(new HashMap<String, String>() {{
            put("tag1", "1");
            put("tag2", "2");
        }});


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(event);

        String expected = "{\"name\":\"name\",\"fields\":{\"field1\":\"1\",\"field\":true},\"tags\":{\"tag1\":\"1\",\"tag2\":\"2\"}}";
        assertEquals(expected, json);

    }

}
