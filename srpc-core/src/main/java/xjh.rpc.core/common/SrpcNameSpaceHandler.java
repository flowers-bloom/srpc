package xjh.rpc.core.common;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import xjh.rpc.core.consumer.ReferenceBean;
import xjh.rpc.core.consumer.ReferenceDefinitionParser;
import xjh.rpc.core.provider.ServiceBean;
import xjh.rpc.core.provider.ServiceBeanDefinitionParser;
import xjh.rpc.core.registry.RegistryConfig;
import xjh.rpc.core.registry.RegistryDefinitionParser;

/**
 * @author XJH
 * @date 2020/11/26
 */
public class SrpcNameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryDefinitionParser(RegistryConfig.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser(ServiceBean.class));
        registerBeanDefinitionParser("reference", new ReferenceDefinitionParser(ReferenceBean.class));
    }
}
