package xjh.rpc.test.dp.chain;

/**
 * @Author XJH
 * @Date 2020/11/14
 * @Description 处理 Integer 类型消息
 */
public class HandlerB extends BaseHandler<Integer> {
    private Handler next;

    @Override
    public void setNext(Handler next) {
        super.setNext(next);
    }

    @Override
    public void handler(Object request) {
        if (super.match(request)) {
            System.out.println("HandlerB catch request");
        }

        super.handler(request);
    }
}
