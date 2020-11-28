package xjh.rpc.test.dp.chain;

import java.lang.reflect.ParameterizedType;

/**
 * @Author XJH
 * @Date 2020/11/14
 * @Description 基础处理器
 */
public class BaseHandler<I> implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handler(Object request) {
        if (next != null) {
            this.next.handler(request);
        }
    }

    /**
     * 获取 I 的具体类型
     *
     * @return I
     */
    private Class<I> getRealClass() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<I>) pt.getActualTypeArguments()[0];
    }

    /**
     * 判断是否是需要处理的消息
     *
     * @param request
     * @return boolean
     */
    public final boolean match(Object request) {
        return getRealClass().isInstance(request);
    }
}
