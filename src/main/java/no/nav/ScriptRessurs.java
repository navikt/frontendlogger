package no.nav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Component
@Path("/logger.js")
public class ScriptRessurs {
    private static final Logger logger = LoggerFactory.getLogger(ScriptRessurs.class);
    private static final String basescript = readfile();

    @GET
    public Response file(@QueryParam("appname") String appname) {
        if (appname == null || appname.isEmpty()) {
            return Response
                    .status(BAD_REQUEST)
                    .entity("'appname' must be defined as queryparameters")
                    .build();
        }

        String filecontent = createFile(appname);
        return Response.ok(filecontent, "application/javascript").build();
    }

    private static String readfile() {
        try {
            URI script = ClassLoader.getSystemResource("loggerbase.js").toURI();
            return new String(Files.readAllBytes(Paths.get(script)), "UTF-8");
        } catch (Exception e) {
            logger.error("Could not read scriptbase", e);
            return "";
        }
    }

    private static String createFile(String appname) {
        String script = format("%s %s",
                jsVariable("appname", appname),
                basescript);

        return jsFunctionScope(script);
    }

    private static String jsFunctionScope(String script) {
        return format("(function(){ %s })();", script);
    }

    private static String jsVariable(String name, String value) {
        return format("var %s = '%s';", name, value);
    }
}
