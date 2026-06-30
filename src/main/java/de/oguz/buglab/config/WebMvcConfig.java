package de.oguz.buglab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final BugTrackerInterceptor bugTrackerInterceptor;

    public WebMvcConfig(BugTrackerInterceptor bugTrackerInterceptor) {
        this.bugTrackerInterceptor = bugTrackerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bugTrackerInterceptor);
    }
}
