package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: test
 * @description: jvm测试类
 * @author: wangtao15
 * @create: 2018-09-29 15:20
 **/
public class JvmStart {
    public static void main(String[] args) {
        List<Demo> demoList = new ArrayList<Demo>();
        try {
            Thread.sleep(35000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 1;
        while(i++ < 1000){
            demoList.add(new Demo());
        }
    }
}
