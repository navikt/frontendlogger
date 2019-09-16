package no.nav;

import static java.util.concurrent.TimeUnit.SECONDS;

import static javax.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;
import static no.nav.RateLimiterFilter.MAX_WAIT_TIME_IN_SECONDS;
import static no.nav.RateLimiterFilter.PATH_TO_LIMIT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterFilterTest {

    private RateLimiter rateLimiter = mock(RateLimiter.class);

    @Before
    public void setUp() {
        RateLimiterFilter.rateLimiter = rateLimiter;
    }

    @Test
    public void skal_gi_too_many_requests_ved_ventetid_mer_enn_definert_maks_ventetid() throws IOException {
        when(rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)).thenReturn(false);
        
        ContainerRequestContext requestContext = requestContext(PATH_TO_LIMIT + "blabla");
        new RateLimiterFilter().filter(requestContext);
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(TOO_MANY_REQUESTS.getStatusCode());
    }
    
    @Test
    public void skal_godta_mindre_ventetid_enn_maks() throws IOException {
        when(rateLimiter.tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS)).thenReturn(true);
        
        ContainerRequestContext requestContext = requestContext(PATH_TO_LIMIT + "blabla");
        new RateLimiterFilter().filter(requestContext);
        verify(rateLimiter).tryAcquire(MAX_WAIT_TIME_IN_SECONDS, SECONDS);
        verify(requestContext, never()).abortWith(any(Response.class));
    }
    
    @Test
    public void skal_ikke_begrense_requester_mot_annen_path_enn_api() throws IOException {
        
        new RateLimiterFilter().filter(requestContext("/frontendlogger/internal/blabla"));
        verifyZeroInteractions(rateLimiter);
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
}
