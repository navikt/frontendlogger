package no.nav;

import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.google.common.util.concurrent.RateLimiter;

@Provider
public class RateLimiterFilter implements ContainerRequestFilter {

    static final String PATH_TO_LIMIT = "/frontendlogger/api/";
    static final int MAX_WAIT_TIME_IN_SECONDS = 2;
    static RateLimiter rateLimiter = RateLimiter.create(50.0);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getRequestUri().getPath();
        if(path.startsWith(PATH_TO_LIMIT) && !rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)) {
            requestContext.abortWith(status(TOO_MANY_REQUESTS).build());
        }
    }

}
