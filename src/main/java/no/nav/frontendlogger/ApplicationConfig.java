package no.nav.frontendlogger;


import lombok.extern.slf4j.Slf4j;
import no.nav.common.log.LogFilter;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.EnvironmentUtils.isDevelopment;
import static no.nav.common.utils.EnvironmentUtils.requireApplicationName;

@Slf4j
@Configuration
public class ApplicationConfig  {

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
        registration.addUrlPatterns("/api/*");
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
}
