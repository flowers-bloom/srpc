package xjh.rpc.test.dp.chain;

/**
 * @Author XJH
 * @Date 2020/11/14
 * @Description
 */
public class HandlerC extends BaseHandler<Character> {
    private Handler next;

    @Override
    public void setNext(Handler next) {
        super.setNext(next);
    }

    @Override
    public void handler(Object request) {
        if (super.match(request)) {
            System.out.println("HandlerC catch request");
        }

        super.handler(request);
    }
}
