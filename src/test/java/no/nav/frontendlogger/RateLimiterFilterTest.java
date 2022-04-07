package no.nav.frontendlogger;

import com.google.common.util.concurrent.RateLimiter;
import no.nav.frontendlogger.RateLimiterFilter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

import static java.util.concurrent.TimeUnit.SECONDS;
import static no.nav.frontendlogger.RateLimiterFilter.MAX_WAIT_TIME_IN_SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class RateLimiterFilterTest {

    private RateLimiter rateLimiter = mock(RateLimiter.class);

    @Before
    public void setUp() {
        RateLimiterFilter.rateLimiter = rateLimiter;
    }

    @Test
    public void skal_gi_too_many_requests_ved_ventetid_mer_enn_definert_maks_ventetid() throws IOException, ServletException {
        when(rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)).thenReturn(false);

        ServletRequest request = request();
        ServletResponse response = response();
        FilterChain filterChain = filterChain();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            new RateLimiterFilter().doFilter(request, response, filterChain);
        });
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatus());
        verify(rateLimiter).tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS);
    }

    @Test
    public void skal_godta_mindre_ventetid_enn_maks() throws IOException, ServletException {
        when(rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)).thenReturn(true);

        ServletRequest request = request();
        ServletResponse response = response();
        FilterChain filterChain = filterChain();
        new RateLimiterFilter().doFilter(request, response, filterChain);
        verify(rateLimiter).tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS);
        verify(filterChain).doFilter(request, response);
    }

    private ContainerRequestContext requestContext(String path) {
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        UriInfo uriInfo = mock(UriInfo.class);
        URI requestUri = URI.create(path);
        when(uriInfo.getRequestUri()).thenReturn(requestUri);
        when(uriInfo.getPath()).thenReturn(path);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        return requestContext;
    }

    private ServletRequest request() {
        return mock(ServletRequest.class);
    }

    private ServletResponse response() {
        return mock(ServletResponse.class);
    }

    private FilterChain filterChain() {
        return mock(FilterChain.class);
    }
}
