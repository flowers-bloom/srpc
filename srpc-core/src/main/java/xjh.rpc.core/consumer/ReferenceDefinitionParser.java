package xjh.rpc.core.consumer;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author XJH
 * @date 2020/11/27
 */
public class ReferenceDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private Class<ReferenceBean> clazz;

    public ReferenceDefinitionParser(Class<ReferenceBean> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return clazz;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String inf = element.getAttribute("interface");

        if (inf != null && !"".equals(inf)) {
            builder.addPropertyValue("inf", inf);
        }
    }
}
