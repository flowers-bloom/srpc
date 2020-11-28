package xjh.rpc.serialization.impl.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import xjh.rpc.serialization.Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author XJH
 * @date 2020/11/28
 */
public class KryoImpl implements Serialization {
    private Kryo kryo;

    public KryoImpl() {
        this.kryo = new Kryo();
    }

    @Override
    public <T> T serialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);

        return kryo.readObject(input, clazz);
    }

    @Override
    public byte[] deserialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);

        kryo.writeObject(output, object);
        output.flush();

        return bos.toByteArray();
    }
}
