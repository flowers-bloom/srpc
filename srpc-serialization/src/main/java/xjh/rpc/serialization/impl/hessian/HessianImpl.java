package xjh.rpc.serialization.impl.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import xjh.rpc.serialization.Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author XJH
 * @date 2020/11/28
 */
public class HessianImpl implements Serialization {

    @SuppressWarnings("unchecked")
    public <T> T serialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        HessianInput input = new HessianInput(bis);
        Object object = null;

        try {
            object = input.readObject(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (T) object;
    }

    public byte[] deserialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(bos);

        try {
            output.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }
}
