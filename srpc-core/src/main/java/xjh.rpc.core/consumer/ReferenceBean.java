package xjh.rpc.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xjh.rpc.core.registry.RegistryConfig;

/**
 * @author XJH
 * @date 2020/11/27
 */
@Slf4j
public class ReferenceBean implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private String id;
    private String inf;
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("consumer spring context initialized complete");

        refer();
    }

    private void refer() {
        RegistryConfig registryConfig = (RegistryConfig) context.getBean("registry");
        new Consumer(registryConfig.getAddress());
    }
}
