package xjh.rpc.serialization.impl.fastjson;

import com.alibaba.fastjson.JSONObject;
import xjh.rpc.serialization.Serialization;

/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description FastJson 序列化
 */
public class FastJsonImpl implements Serialization {
    public static Serialization serialization = new FastJsonImpl();

    @Override
    public <T> T serialize(byte[] bytes, Class<T> clazz) {
        return JSONObject.parseObject(bytes, clazz);
    }

    @Override
    public byte[] deserialize(Object object) {
        return JSONObject.toJSONBytes(object);
    }
}
