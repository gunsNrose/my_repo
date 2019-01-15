package test2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: test
 * @description: test Bean set util
 * @author: wangtao15
 * @create: 2018-09-10 17:31
 **/
public class Bean {

    private static AtomicInteger ai = new AtomicInteger(1);
    /**
     * 字段1
     */
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public synchronized void doSomething(){
        System.out.println("parent: in"+ai.incrementAndGet());
    }

}
