package test2;

/**
 * @program: test
 * @description: 线程2
 * @author: wangtao15
 * @create: 2018-11-28 17:22
 **/
public class T2  extends Thread{
    @Override
    public void run(){
        BeanSon a = new BeanSon();
        synchronized (a){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a.doSomething();
        }
    }

}