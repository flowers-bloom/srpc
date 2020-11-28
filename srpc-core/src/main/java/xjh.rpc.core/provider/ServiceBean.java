package xjh.rpc.core.provider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xjh.rpc.core.registry.RegistryConfig;
import xjh.rpc.transport.common.Endpoint;

/**
 * @author XJH
 * @date 2020/11/26
 */
@Data
@Slf4j
public class ServiceBean implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private String inf;
    private String ref;
    private ApplicationContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("provider spring context initialized complete");

        export();
    }

    private void export() {
        RegistryConfig registryConfig = (RegistryConfig) context.getBean("registry");
        new Provider(new Endpoint("127.0.0.1:9000"), true,
                registryConfig.getAddress());
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
