package xjh.rpc.core.util;

import java.util.Random;

/**
 * @author XJH
 * @date 2020/11/28
 */
public class RandomUtil {
    private static final Random random = new Random();

    /**
     * 返回 [0, bound) 内随机一个数
     *
     * @param bound
     * @return
     */
    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
