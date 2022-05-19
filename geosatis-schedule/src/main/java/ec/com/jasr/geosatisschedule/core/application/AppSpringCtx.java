package ec.com.jasr.geosatisschedule.core.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppSpringCtx implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    public void setApplicationContext(ApplicationContext context) {
        if (appCtx == null) {
            appCtx = context;
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        return appCtx.getBean(requiredType);
    }

    public static ApplicationContext getAppCtx() {
        return appCtx;
    }

}
