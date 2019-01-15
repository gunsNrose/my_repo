package test2;

/**
 * @program: test
 * @description: as
 * @author: wangtao15
 * @create: 2018-10-09 20:35
 **/

public class BeanSon extends Bean {

    public void say(){
        int i = 11;
        System.out.println("aa  "+i);
    }

    @Override
    public synchronized void doSomething(){
        System.out.println("son: in");
        super.doSomething();
        System.out.println("son: out");
    }
}
