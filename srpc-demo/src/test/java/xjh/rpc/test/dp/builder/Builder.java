package xjh.rpc.test.dp.builder;

import lombok.ToString;

/**
 * @author XJH
 * @date 2020/11/14
 *
 * 类生成器抽象父类
 * 定义了子类构造所需的所有配置方法，将构造细节从子类中分离
 */
@ToString
public abstract class Builder<I> {
    private String fields;
    private String methods;
    private String innerClass;

    /**
     * 返回构造的目标对象
     * @return I
     */
    private I self() {
        return (I) this;
    }

    public I fields(String fields) {
        this.fields = fields;
        return self();
    }

    public I methods(String methods) {
        this.methods = methods;
        return self();
    }

    public I innerClass(String innerClass) {
        this.innerClass = innerClass;
        return self();
    }
}
