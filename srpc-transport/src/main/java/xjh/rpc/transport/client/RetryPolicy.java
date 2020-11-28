package xjh.rpc.transport.client;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description 断线重连的重试策略
 */
public interface RetryPolicy {
    /**
     * 是否允许尝试重连
     *
     * @return
     */
    boolean allowRetry();

    /**
     * 获取重连失败后的等待时间，单位秒
     *
     * @return
     */
    int getWaitSecond();

    /**
     * 重传次数自增
     */
    void retryCountIncr();

    /**
     * 获取重连次数
     * @return
     */
    int getRetryCount();

    /**
     * 重置重连次数
     */
    void resetRetryCount();
}
