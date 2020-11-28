package xjh.rpc.transport.client;


import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description 有限次数的重连策略
 */
@Slf4j
public class LimitedCountRetryPolicy implements RetryPolicy {

    /**
     * 最大的可重试次数
     */
    public int maxRetryCount;

    /**
     * 当前的重试次数
     */
    public int retryCount;
    private Random random = new Random();

    public LimitedCountRetryPolicy(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        this.retryCount = 0;
    }

    @Override
    public boolean allowRetry() {
        return retryCount <= maxRetryCount;
    }

    @Override
    public int getWaitSecond() {
        return random.nextInt(3) + 1;
    }

    @Override
    public void retryCountIncr() {
        ++this.retryCount;
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void resetRetryCount() {
        this.retryCount = 0;
    }
}
