package com.splitmate.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }

    /**  
     * Call this from anywhere (e.g. JavaFX controllers) to get the Spring context.  
     */
    public static ApplicationContext get() {
        return context;
    }
}
