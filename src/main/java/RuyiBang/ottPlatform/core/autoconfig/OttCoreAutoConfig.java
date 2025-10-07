package RuyiBang.ottPlatform.core.autoconfig;

import RuyiBang.ottPlatform.core.web.CorrelationIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OttCoreAutoConfig {
    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
        FilterRegistrationBean<CorrelationIdFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new CorrelationIdFilter());
        reg.setOrder(Integer.MIN_VALUE);
        return reg;
    }
}
