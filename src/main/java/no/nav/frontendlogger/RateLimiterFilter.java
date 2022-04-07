package no.nav.frontendlogger;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RateLimiterFilter implements Filter {

    static final int MAX_WAIT_TIME_IN_SECONDS = 2;
    static RateLimiter rateLimiter = RateLimiter.create(100.0);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(!rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
        }
        filterChain.doFilter(request, response);
    }
}
