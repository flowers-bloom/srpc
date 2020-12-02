package xjh.rpc.common.spi;

import java.lang.annotation.*;

/**
 * @author XJH
 * @date 2020/12/01
 *
 * 声明默认实现类注解
 */
@Documented
// 可用于类的任何元素
@Target(ElementType.TYPE)
// 标记的注释由JVM保留，因此运行时环境可以使用它
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {
    /**
     * 默认使用的实现类
     * @return
     */
    String value();
}
