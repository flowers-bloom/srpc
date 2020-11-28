package xjh.rpc.core.common;

import java.util.concurrent.CountDownLatch;

/**
 * @Author XJH
 * @Date 2020/11/14
 * @Description 用于线程间通信
 */
public class SyncFuture<T> {
    /**
     * 阻塞对象
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * 共享对象
     */
    private T t;

    /**
     * 写入响应对象
     *
     * @param t
     */
    public void complete(T t) {
        this.t = t;
        latch.countDown();
    }

    /**
     * 返回响应对象
     *
     * @return T
     */
    public T get() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.t;
    }
}
