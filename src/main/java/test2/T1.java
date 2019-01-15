package test2;

/**
 * @program: test
 * @description: 线程1
 * @author: wangtao15
 * @create: 2018-11-28 17:21
 **/
public class T1  extends Thread{

    @Override
    public void run(){
        Integer i;
        Bean a2 = new Bean();
        a2.doSomething();
    }

}
