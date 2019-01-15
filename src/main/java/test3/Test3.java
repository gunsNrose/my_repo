package test3;

import test2.Bean;

import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @program: test
 * @description: a
 * @author: wangtao15
 * @create: 2018-12-27 11:21
 **/
public class Test3 {
    public static void main(String[] args) {
        try {
            Method m = Bean.class.getDeclaredMethod("setField",String.class);
            System.out.println(methodToProperty("seta"));
            System.out.println("A".substring(0, 1).toLowerCase(Locale.ENGLISH));
            System.out.println("   :"+"A".substring(1));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            System.out.println("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    private static String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }
}
