package xjh.rpc.core.provider;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author XJH
 * @date 2020/11/26
 */
public class ServiceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private Class<?> clazz;

    public ServiceBeanDefinitionParser(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return clazz;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String inf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");

        if (inf != null && !"".equals(inf)) {
            builder.addPropertyValue("inf", inf);
        }

        if (ref != null && !"".equals(ref)) {
            builder.addPropertyValue("ref", inf);
        }
    }
}
