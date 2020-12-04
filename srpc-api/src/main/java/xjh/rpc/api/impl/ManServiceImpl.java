package xjh.rpc.api.impl;

import lombok.Data;
import xjh.rpc.api.GreetService;
import xjh.rpc.api.PersonService;

/**
 * @author XJH
 * @date 2020/12/04
 */
@Data
public class ManServiceImpl implements PersonService {
    private GreetService greetService;

    @Override
    public void say() {
        System.out.println(greetService.sayHello("i am man"));
    }
}
