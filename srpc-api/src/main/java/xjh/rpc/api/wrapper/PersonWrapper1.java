package xjh.rpc.api.wrapper;

import xjh.rpc.api.PersonService;
import xjh.rpc.api.impl.ManServiceImpl;

/**
 * @author XJH
 * @date 2020/12/04
 */
public class PersonWrapper1 implements PersonService {
    private ManServiceImpl manService;

    public PersonWrapper1(ManServiceImpl manService) {
        this.manService = manService;
    }

    @Override
    public void say() {
        System.out.println("------PersonWrapper1------");
        manService.say();
        System.out.println("------PersonWrapper1------");
    }
}
