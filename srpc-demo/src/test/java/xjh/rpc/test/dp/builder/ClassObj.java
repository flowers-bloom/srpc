package xjh.rpc.test.dp.builder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author XJH
 * @Date 2020/11/15
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassObj extends Builder<ClassObj> {
    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("");
        context.start();
    }
}
