package xjh.rpc.core.registry;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author XJH
 * @date 2020/11/27
 */
public class RegistryDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private Class clazz;

    public RegistryDefinitionParser(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return clazz;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String address = element.getAttribute("address");

        if (address != null && !"".equals(address)) {
            builder.addPropertyValue("address", address);
        }
    }
}
