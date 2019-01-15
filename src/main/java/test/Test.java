package test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws NoSuchMethodException {
//        Map<String,Object> param = new HashMap<String, Object>();
//        List<String> rspList = new ArrayList<String>();
//        param.put("offerIds",rspList);
//        Object list = param.get("offerIds");
//        if(list != null){
//            List<String> offerIds = (List<String>) list;
//            if(offerIds.size() == 0){
//                System.out.println("000");
//            }
//        }
//        String a = "返回码未配置[11200315]:[查询失败:操作BUSIOPER_ID不能为空]";
//        System.out.println(getReturnMegFromCRM(a));
//        Class<SomeObject> c = SomeObject.class;
//        Method method = c.getMethod("doSomething");
//        if(method.isAnnotationPresent(Debug.class)) {
//            System.out.println("@Debug is found.");
//            Debug debug = method.getAnnotation(Debug.class);
//            System.out.println("\tvalue = " + debug.value());
//            System.out.println("\tname = " + ());
//        }
//        else {
//            System.out.println("@Debug is not found.");
//        }
//        Annotation[] annotations = method.getAnnotations();
//        for(Annotation annotation : annotations) {
//            System.out.println(annotation.annotationType().getName());
//        }
    }

    private static String getReturnMegFromCRM2(String respDesc){
        //通过正则表达式去掉crm侧返回报文中的 返回码未配置[*********]:
        String pattern = "^返回码未配置(\\[|【)[a-zA-Z0-9]+(\\]|】)(:|：)((\\[|【)[a-zA-Z0-9]+(\\]|】))";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(respDesc);
        if(m.find()){
            respDesc = m.group(3);
//            respDesc = respDesc.replace(m.group(0),"");
//            if(respDesc != null && !"".equals(respDesc) && respDesc.length() >= 3){
//                //去掉返回错误内的首字符[/【和尾字符]/】
//                char frist = respDesc.charAt(0);
//                char last = respDesc.charAt(respDesc.length()-1);
//                if((frist == '[' || frist == '【') && (last == ']' || last == '】')){
//                    respDesc = respDesc.substring(1,respDesc.length()-1);
//                }
//            }
        }
        return respDesc;
    }

    private static String getReturnMegFromCRM(String respDesc){
        //通过正则表达式去掉crm侧返回报文中的 返回码未配置[*********]:
        String pattern = "^返回码未配置(\\[|【)[a-zA-Z0-9]+(\\]|】)(:|：)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(respDesc);
        if(m.find()){
            respDesc = respDesc.replace(m.group(0),"");
            if(respDesc != null && !"".equals(respDesc) && respDesc.length() >= 3){
                //去掉返回错误内的首字符[/【和尾字符]/】
                char frist = respDesc.charAt(0);
                char last = respDesc.charAt(respDesc.length()-1);
                if((frist == '[' || frist == '【') && (last == ']' || last == '】')){
                    respDesc = respDesc.substring(1,respDesc.length()-1);
                }
            }
        }
        return respDesc;
    }
}
