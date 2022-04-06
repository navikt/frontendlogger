package no.nav.frontendlogger;


import no.nav.common.log.LogFilter;
import no.nav.common.metrics.InfluxClient;
import no.nav.common.metrics.MetricsClient;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static no.nav.common.utils.EnvironmentUtils.*;

@Configuration
public class ApplicationConfig {

    @Bean
    public FilterRegistrationBean logFilterRegistrationBean() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter(requireApplicationName(), isDevelopment().orElse(false)));
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean setRateLimiterFilterFilterRegistrationBean() {
        FilterRegistrationBean<RateLimiterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimiterFilter());
        registration.setOrder(2);
        registration.addUrlPatterns("/api/**");
        return registration;
    }

    @Bean
    public FilterRegistrationBean setStandardHeadersFilterRegistrationBean() {
        FilterRegistrationBean<SetStandardHttpHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SetStandardHttpHeadersFilter());
        registration.setOrder(3);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public PinpointClient pinpointClient() {
        return new PinpointClient();
    }

    @Bean
    public MetricsClient metricsClient() {
        return new InfluxClient();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                getOptionalProperty("CORS_ALLOWED_ORIGINS")
                        .ifPresent(allowedOrigins -> {
                            registry.addMapping("/api/**")
                                    .allowedHeaders(
                                            "Accept",
                                            "Accept-language",
                                            "Content-Language",
                                            "Content-Type",
                                            "Nav-Consumer-Id")
                                    .allowCredentials(true)
                                    .allowedOriginPatterns(allowedOrigins);
                        });

            }
        };
    }
}
