package xjh.rpc.test.dp.chain;

/**
 * @Author XJH
 * @Date 2020/11/14
 * @Description 责任链模式
 */
public interface Handler {
    /**
     * 设置下一个处理器
     *
     * @param next
     */
    void setNext(Handler next);

    /**
     * 处理请求
     *
     * @param request
     */
    void handler(Object request);
}
