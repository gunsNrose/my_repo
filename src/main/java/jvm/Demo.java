package jvm;

/**
 * @program: test
 * @description: demo
 * @author: wangtao15
 * @create: 2018-09-29 15:21
 **/
public class Demo {
    /**
     * aa
     */
    private static String a = "";

    static{
        System.out.println(a);
    }


    public static void main(String[] args) {
        String aa = Demo.a;
        System.out.println("2121");
    }

}
