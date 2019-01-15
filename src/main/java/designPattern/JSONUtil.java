package designPattern;

import net.sf.json.*;
import net.sf.json.util.JavaIdentifierTransformer;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 对于传值和接收的值进行格式化
 * 
 * @author zWX326352 2016-3-22 10:50:33
 */
@SuppressWarnings("unchecked")
public class JSONUtil
{
    private static final String JSONOBJECT = "JSONOBJECT"; // 对象
    
    private static final String JSONARRAY = "JSONARRAY"; // 集合
    
    private static final String NORMAL = "NORMAL"; // 普通
    

    
    /**
     * 根据返回值转换成map集合
     * 
     * @param json json字符串,格式如 {"id":1001, "name":"张三"}
     * @return map集合
     */
    public static Map<String, Object> jsonToMapForKF(String json)
    {
        Map<String, Object> map = null;
        try
        {
            map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.fromObject(json);
            Iterator<String> keyIterator = jsonObject.keys();
            while (keyIterator.hasNext())
            {
                String key = (String)keyIterator.next();
                Object value = jsonObject.get(key);
                map.put(key, value == null || JSONNull.getInstance().equals(value) ? null : value);
            }
        }
        catch (JSONException e)
        {
            return null;
        }
        return map;
    }
    
    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject)
        throws JSONException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext())
        {
            String key = (String)keyIterator.next();
            Object value = jsonObject.get(key);
            map.put(key, value == null || JSONNull.getInstance().equals(value) ? null : value);
        }
        
        return map;
    }
    
    /**
     * 根据返回值转换成list集合
     * 
     * @param json json字符串,格式如[{}, {}]
     * @return list集合
     */
    public static List<Map<String, Object>> jsonToListMap(String json)
    {
        List<Map<String, Object>> list = null;
        try
        {
            list = new ArrayList<Map<String, Object>>();
            JSONArray array = JSONArray.fromObject(json);
            for (int i = 0; i < array.size(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                list.add(jsonToMapForKF(jsonObject.toString()));
            }
        }
        catch (JSONException e)
        {
            return null;
        }
        return list;
    }
    
    /**
     * 解析比较复杂的map-json字符串 包含各种数据类型 字符串 布尔型 对象
     * 
     * @param json json字符串
     * @return map集合
     */
    public static <T> Map<String, Object> jsonToComplexMap(String json, Class<T> cls)
    {
        Map<String, Object> map = null;
        if (getJsonType(json) == JSONOBJECT)
        {// 判断是否都是对象格式
            map = jsonMapToMap(json, cls);
        }
        return map;
    }
    
    /**
     * 解析比较复杂的list-json字符串 包含多个同类型的数组
     * 
     * @param json json字符串
     * @return list集合
     */
    public static <T> List<T> jsonToComplexList(String json, Class<T> cls)
    {
        List<T> list = null;
        if (getJsonType(json) == JSONARRAY)
        {// 判断是否都是数组格式
            list = jsonListToList(json, cls);
        }
        return list;
    }
    
    public static List<Map<String, Object>> jsonToComplexListMap(String json)
    {
        @SuppressWarnings("rawtypes")
        List list = null;
        if (getJsonType(json) == JSONARRAY)
        {// 判断是否都是数组格式
            list = jsonListToList(json, Map.class);
        }
        return list;
    }
    
    /**
     * 包含多层map的字符串解析
     * 
     * @param json json字符串
     * @return map集合
     */
    public static <T> Map<String, Object> jsonMapToMap(String json, Class<T> cls)
    {
        Map<String, Object> map = null;
        try
        {
            map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.fromObject(json);
            Iterator<String> keyIterator = jsonObject.keys();
            while (keyIterator.hasNext())
            {
                String key = (String)keyIterator.next();
                Object value = jsonObject.get(key);
                if (value != null && !"".equals(value))
                {
                    String val = value.toString();
                    if (getJsonType(val) == JSONOBJECT)
                    {// object
                        value = jsonMapToMap(val, cls);// 迭代
                    }
                    else if (getJsonType(val) == JSONARRAY)
                    {// list
                        value = jsonListToList(val, cls);
                    }
                }
                map.put(key, value == null || JSONNull.getInstance().equals(value) ? null : value);
            }
        }
        catch (JSONException e)
        {
            return null;
        }
        
        return map;
    }
    
    /**
     * 包含多层list的字符串解析
     * 
     * @param json json字符串
     * @return list集合
     */
    public static <T> List<T> jsonListToList(String json, Class<T> cls)
    {
        List<T> list = null;
        try
        {
            list = new ArrayList<T>();
            JSONArray array = JSONArray.fromObject(json);
            // String simpleName = cls.getSimpleName();
            for (int i = 0; i < array.size(); i++)
            {
                Object value = array.get(i);
                /*
                 * if ("Boolean".equals(simpleName) || "Byte".equals(simpleName) || "Character".equals(simpleName) ||
                 * "Short".equals(simpleName) || "Integer".equals(simpleName) || "Long".equals(simpleName) ||
                 * "Float".equals(simpleName) || "Double".equals(simpleName)) { value = array.get(i); } else { value =
                 * array.getJSONObject(i); }
                 */
                
                String val = value == null ? null : value.toString();
                if (getJsonType(val) == JSONOBJECT)
                {// object
                    value = jsonMapToMap(val, cls);
                }
                else if (getJsonType(val) == JSONARRAY)
                {
                    value = jsonListToList(val, cls); // 迭代
                }
                
                list.add(value == null || JSONNull.getInstance().equals(value) ? null : (T)value);
            }
        }
        catch (JSONException e)
        {
            return null;
        }
        return list;
    }
    
    public static Map<String, Object> mapStringToObject(Map<String, String> params)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            Object obj = value;
            if (getJsonType(value) == JSONOBJECT)
            {
                obj = jsonMapToMap(value, Map.class);
            }
            else if (getJsonType(value) == JSONARRAY)
            {
                obj = jsonListToList(value, List.class);
            }
            map.put(key, obj);
        }
        return map;
    }
    
    public static String getJsonTypeBak(String jsonStr)
    {
        String type = NORMAL;
        if (StringUtils.isNotBlank(jsonStr))
        {
            try
            {
                JSONObject.fromObject(jsonStr);
                type = JSONOBJECT;
            }
            catch (JSONException e)
            {
                try
                {
                    JSONArray.fromObject(jsonStr);
                    type = JSONARRAY;
                }
                catch (JSONException e1)
                {
                    type = NORMAL;
                }
                
            }
        }
        return type;
    }
    
    public static String getJsonType(String jsonStr)
    {
        String type = NORMAL;
        if (StringUtils.isNotBlank(jsonStr))
        {
            if (jsonStr.startsWith("{"))
            {
                type = JSONOBJECT;
            }
            else if (jsonStr.startsWith("["))
            {
                type = JSONARRAY;
            }
            else
            {
                type = NORMAL;
            }
        }
        return type;
    }
    
    /**
     * 对象转换成JSON字符串
     * 
     * @param obj 需要转换的对象
     * @return 对象的string字符
     */
    public static String objectToJson(Object obj)
    {
        JSONObject jSONObject = JSONObject.fromObject(obj);
        return jSONObject.toString();
    }
    
    /**
     * JSON字符串转换成对象
     * 
     * @param jsonString 需要转换的字符串
     * @param type 需要转换的对象类型
     * @return 对象
     */
    public static <T> T jsonToObject(String jsonString, Class<T> type)
    {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        JsonConfig config = new JsonConfig();
        config.setJavaIdentifierTransformer(new JavaIdentifierTransformer()
        {
            
            @Override
            public String transformToJavaIdentifier(String s)
            {
                char[] chars = s.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        });
        
        config.setRootClass(type);
        
        config.setClassMap(getClassMap(type));
        
        return (T)JSONObject.toBean(jsonObject, config);
    }
    
    public static <T> T jsonToObject(String jsonString, Class<T> type, String key)
    {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        if (key != null && !"".equals(key))
        {
            JSONObject json = jsonObject.getJSONObject(key);
            return (T)JSONObject.toBean(json, type);
        }
        return (T)JSONObject.toBean(jsonObject, type);
    }
    
    /**
     * 将JSONArray对象转换成list集合
     * 
     * @param jsonArr
     * @return
     */
    public static List<Object> jsonToList(JSONArray jsonArr)
    {
        List<Object> list = new ArrayList<Object>();
        for (Object obj : jsonArr)
        {
            if (obj instanceof JSONArray)
            {
                list.add(jsonToList((JSONArray)obj));
            }
            else if (obj instanceof JSONObject)
            {
                list.add(jsonToMap((JSONObject)obj));
            }
            else
            {
                list.add(obj);
            }
        }
        return list;
    }
    
    /**
     * 将JSONArray对象转换成list集合
     * 
     * @param <T>
     * 
     * @param jsonStr
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> type)
    {
        List<T> list = new ArrayList<T>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for (Object obj : jsonArray)
        {
            if (obj instanceof JSONArray)
            {
                list.add((T)jsonToList(obj.toString(), type));
            }
            else if (obj instanceof JSONObject)
            {
                list.add(jsonToObject(obj.toString(), type));
            }
            else
            {
                list.add((T)obj);
            }
            
        }
        return list;
    }
    
    /**
     * 将JSONArray对象转换成list集合
     * 
     * @param <T>
     * 
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> jsonToList(String jsonStr, Class<T> type, Map<String, Class> map)
    {
        List<T> list = new ArrayList<T>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for (Object obj : jsonArray)
        {
            if (obj instanceof JSONArray)
            {
                list.add((T)jsonToList(obj.toString(), type));
            }
            else if (obj instanceof JSONObject)
            {
                list.add(jsonToObject(obj.toString(), type));
            }
            else
            {
                list.add((T)obj);
            }
            
        }
        return list;
    }
    
    @SuppressWarnings("rawtypes")
    public static <T> T jsonToObject(String jsonString, Class<T> type, Map<String, Class> map)
    {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        JsonConfig config = new JsonConfig();
        config.setJavaIdentifierTransformer(new JavaIdentifierTransformer()
        {
            
            @Override
            public String transformToJavaIdentifier(String s)
            {
                char[] chars = s.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        });
        
        config.setRootClass(type);
        return (T)JSONObject.toBean(jsonObject, type, map);
    }
    
    /**
     * 将json字符串转换成map对象
     * 
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap1(String json)
    {
        JSONObject obj = JSONObject.fromObject(json);
        return jsonToMap(obj);
    }
    
    /**
     * 将JSONObject转换成map对象
     * 
     * @param obj
     * @return
     */
    public static Map<String, Object> jsonToMap(JSONObject obj)
    {
        Set<?> set = obj.keySet();
        Map<String, Object> map = new HashMap<String, Object>(set.size());
        for (Object key : obj.keySet())
        {
            Object value = obj.get(key);
            if (value instanceof JSONArray)
            {
                map.put(key.toString(), jsonToList((JSONArray)value));
            }
            else if (value instanceof JSONObject)
            {
                map.put(key.toString(), jsonToMap((JSONObject)value));
            }
            else
            {
                map.put(key.toString(), obj.get(key));
            }
            
        }
        return map;
    }
    
    public static Map<String, Object> beanToMap(Object obj)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        try
        {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++)
            {
                String name = descriptors[i].getName();
                if (!"class".equals(name))
                {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        }
        catch (Exception e)
        {
        }
        return params;
    }
    
    public static <T> List<Map<String, Object>> beansToMap(List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Iterator<T> it = list.iterator();
            while (it.hasNext())
            {
                Object obj = it.next();
                Map<String, Object> params; //= new HashMap<String, Object>();
                params = beanToMap(obj);
                result.add(params);
            }
            return result;
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public static <T> Map<String, Class> getClassMap(Class<T> type) {
        
        Map<String, Class> classMap = new HashMap<String, Class>();
        getItemClassInList(type, classMap);
        return classMap;
    }
    
    @SuppressWarnings("rawtypes")
    public static <T> void getItemClassInList(Class<T> type, Map<String, Class> classMap){

        Field[] fds = type.getDeclaredFields();                     // 获取字段数组
        for (Field field : fds) {
            String fieldName = field.getName();                     // 获取字段名
            String fieldType = field.getGenericType().toString();   // 获取字段类型
            if (fieldType.contains("java.util.List")) {
                String itemType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));
                try {
                    Class itemClass = Class.forName(itemType);      //List中存放的对象的类型
                    if (!classMap.containsKey(fieldName)) {
                        classMap.put(fieldName, itemClass);                   	
                    }else {
                    	// TODO 记录 bean中重复引用 itemClass 类型的fieldName为避免死循环停止递归
                    	return;   
                    }
                    //递归
                    getItemClassInList(itemClass, classMap);        //递归判断对象中包含的List的类型
                    
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                }
            }else if (fieldType.contains("com.cmos.ngbusicontrol")) {
            	String beanType = fieldType.substring(6);
				try {
					//递归
	            	 Class beanClass = Class.forName(beanType);		//bean 中包含的bean的
	                getItemClassInList(beanClass, classMap);        //递归判断对象中包含的List的类型
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
				}
            }
        }
    }
    
    public static void main(String[] args)
        throws Exception
    {
        // "[{\"categoryId\":1,\"categoryName\":\"饮品\",\"categoryImage\":\"/upload/yinpin.jpg\"},{\"categoryId\":2,\"categoryName\":\"食品\",\"categoryImage\":\"/upload/shiping.jpg\"}]";
        // String jsonStr = "[{\"custShortName\":\"test\",\"custName\":\"abc\",\"custClass\":\"1\","
        // +
        // "\"certTypeName\":\"38974865028450928\",\"certType\":\"1\"},{\"custShortName\":\"test\",\"custName\":\"abc\",\"custClass\":\"1\","
        // + "\"certTypeName\":\"38974865028450928\",\"certType\":\"2\"}]";
        // List<CustInfoBean> bean = jsonToList(jsonStr, CustInfoBean.class);
        // List<Map<String, Object>> list = beansToMap(bean);
        // System.out.println("111");
        
        // OfferInstInfoListBean b1 = new OfferInstInfoListBean();
        // OfferInstInfoBean b2 = new OfferInstInfoBean();
        // List<OfferInstInfoBean> listtt = new ArrayList<OfferInstInfoBean>();
        // b2.setBillingType(54466);
        // b2.setEffectDate("ddddddd");
        // b2.setOfferInstId(123123123);
        // listtt.add(b2);
        // b1.setOfferInstInfo(listtt);
        // List<Map<String, Object>> result = JSONUtil.beansToMap(list)(listtt.toArray());
        // for(Map<String, Object> map:result){
        // for(Entry<String, Object> a :map.entrySet()){
        // System.out.println(a.getKey());
        // System.out.println(a.getValue());
        //
        // }
        // }
        // String jsonStr =
        // "[{\"ExpireDate\":\"test\",\"test\":\"\",\"DoneDate\":null},{\"ExpireDate\":\"test\",\"test\":\"\"}]";
        // JsonConfig config = new JsonConfig();
        // config.setJavaIdentifierTransformer(new JavaIdentifierTransformer()
        // {
        //
        // @Override
        // public String transformToJavaIdentifier(String s)
        // {
        // char[] chars = s.toCharArray();
        // chars[0] = Character.toLowerCase(chars[0]);
        // return new String(chars);
        // }
        // });
        //
        // config.setRootClass(OfferInstInfoBean.class);
        //
        // Object obj = JSONArray.fromObject(jsonStr);
        // List<OfferInstInfoBean> lists = JSONUtil.jsonToList(obj.toString(), OfferInstInfoBean.class);
        //
        //
        // // List<OfferInstInfoBean> list = JSONUtil.jsonToList(jsonStr, OfferInstInfoBean.class);
        // // JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        // // OfferInstInfoBean bean= (OfferInstInfoBean)JSONObject.toBean(jsonObject, config);
        // // OfferInstInfoBean bean = JSONUtil.jsonToObject(jsonStr, OfferInstInfoBean.class);
        // System.out.println(lists);
        String json = "{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":{\"outData\":{\"feeInfo\":[{\"oweFee\":\"0\",\"realBalance\":\"1180\",\"realFee\":\"1516\",\"state\":\"1180\"}]},\"xResultCode\":\"0\",\"xResultInfo\":\"ok\"}}";
        JSONObject result = JSONObject.fromObject(json);
        JSONObject resultString = JSONObject.fromObject(result.get("result"));
        JSONObject responseString = JSONObject.fromObject(resultString);
        System.out.println(responseString.get("xResultCode"));
        System.out.println(responseString.get("xResultInfo"));
        System.out.println(responseString.get("outData").toString());
        List list ;//= jsonToListMap(responseString.get("outData").toString());
        list = JSONUtil.jsonToListMap(responseString.get("outData").toString());
		if(list == null){
			list = new ArrayList<Map>();
			Map tempMap = JSONUtil.jsonToMapForKF(responseString.get("outData").toString());
			list.add(tempMap);
		}
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
        	Map map = (Map)list.get(i);
            System.out.println(map.get("endTime"));
            System.out.println(map.get("scoreNum1"));
            System.out.println(map.get("scoreNum2"));
            System.out.println(map.get("scoreNum3"));
		}
        
    }
    
    /**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param beanCalss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToBean(String jsonString, Class<T> beanCalss) {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		T bean = (T) JSONObject.toBean(jsonObject, beanCalss);

		return bean;

	}

	/**
	 * 将java对象转换成json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJson(Object bean) {

		JSONObject json = JSONObject.fromObject(bean);

		return json.toString();

	}

	/**
	 * 将java对象转换成json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJson(Object bean, String[] _nory_changes, boolean nory) {

		JSONObject json ;

		if (nory) {// 转换_nory_changes里的属性

			Field[] fields = bean.getClass().getDeclaredFields();
			String str = "";
			for (Field field : fields) {
				// System.out.println(field.getName());
				str += (":" + field.getName());
			}
			fields = bean.getClass().getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				// System.out.println(field.getName());
				str += (":" + field.getName());
			}
			str += ":";
			for (String s : _nory_changes) {
				str = str.replace(":" + s + ":", ":");
			}
			json = JSONObject.fromObject(bean, configJson(str.split(":")));

		} else {// 转换除了_nory_changes里的属性

			json = JSONObject.fromObject(bean, configJson(_nory_changes));
		}

		return json.toString();

	}

	private static JsonConfig configJson(String[] excludes) {

		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setExcludes(excludes);
		//
		jsonConfig.setIgnoreDefaultExcludes(false);
		//
		// jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		// jsonConfig.registerJsonValueProcessor(Date.class,
		//
		// new DateJsonValueProcessor(datePattern));

		return jsonConfig;

	}

	/**
	 * 将java对象List集合转换成json字符串
	 * 
	 * @param beans
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String beanListToJson(List beans) {

		StringBuilder rest = new StringBuilder();

		rest.append("[");

		int size = beans.size();

		for (int i = 0; i < size; i++) {

			rest.append(beanToJson(beans.get(i)) + ((i < size - 1) ? "," : ""));

		}

		rest.append("]");

		return rest.toString();

	}

	/**
	 * 
	 * @param beans
	 * @param _nory_changes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String beanListToJson(List beans, String[] _nory_changes, boolean nory) {

		StringBuilder rest = new StringBuilder();

		rest.append("[");

		int size = beans.size();

		for (int i = 0; i < size; i++) {
			try {
				rest.append(beanToJson(beans.get(i), _nory_changes, nory));
				if (i < size - 1) {
					rest.append(",");
				}
			} catch (Exception e) {
			}
			
		}

		rest.append("]");

		return rest.toString();

	}

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 *
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map jsonToMap(String jsonString) {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();
		String key;
		Object value;
		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {

			key = (String) keyIter.next();
			value = jsonObject.get(key).toString();
			valueMap.put(key, value);

		}

		return valueMap;
	}

	/**
	 * map集合转换成json格式数据
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<String, ?> map, String[] _nory_changes, boolean nory) {

		String s_json = "{";

		Set<String> key = map.keySet();
		for (Iterator<?> it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if (map.get(s) == null) {

			} else if (map.get(s) instanceof List<?>) {
				s_json += (s + ":" + JSONUtil.beanListToJson((List<?>) map.get(s), _nory_changes, nory));

			} else {
				JSONObject json = JSONObject.fromObject(map);
				s_json += (s + ":" + json.toString());
				
			}

			if (it.hasNext()) {
				s_json += ",";
			}
		}

		s_json += "}";
		return s_json;
	}

	/**
	 * 从json数组中得到相应java数组
	 *
	 * @param jsonString
	 * @return
	 */
	public static Object[] jsonToObjectArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		return jsonArray.toArray();

	}

	public static String listToJson(List<?> list) {

		JSONArray jsonArray = JSONArray.fromObject(list);

		return jsonArray.toString();

	}

	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 *
	 * @param jsonString
	 * @param beanClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToBeanList(String jsonString, Class<T> beanClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		T bean;
		int size = jsonArray.size();
		List<T> list = new ArrayList<T>(size);

		for (int i = 0; i < size; i++) {

			jsonObject = jsonArray.getJSONObject(i);
			bean = (T) JSONObject.toBean(jsonObject, beanClass);
			list.add(bean);

		}

		return list;

	}

	/**
	 * 从json数组中解析出java字符串数组
	 *
	 * @param jsonString
	 * @return
	 */
	public static String[] jsonToStringArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		int size = jsonArray.size();

		for (int i = 0; i < size; i++) {

			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;
	}

	/**
	 * 从json数组中解析出javaLong型对象数组
	 *
	 * @param jsonString
	 * @return
	 */
	public static Long[] jsonToLongArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		int size = jsonArray.size();
		Long[] longArray = new Long[size];

		for (int i = 0; i < size; i++) {

			longArray[i] = jsonArray.getLong(i);

		}

		return longArray;

	}

	/**
	 * 从json数组中解析出java Integer型对象数组
	 *
	 * @param jsonString
	 * @return
	 */
	public static Integer[] jsonToIntegerArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		int size = jsonArray.size();
		Integer[] integerArray = new Integer[size];

		for (int i = 0; i < size; i++) {

			integerArray[i] = jsonArray.getInt(i);

		}

		return integerArray;

	}

	/**
	 * 从json数组中解析出java Double型对象数组
	 *
	 * @param jsonString
	 * @return
	 */
	public static Double[] jsonToDoubleArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		int size = jsonArray.size();
		Double[] doubleArray = new Double[size];

		for (int i = 0; i < size; i++) {

			doubleArray[i] = jsonArray.getDouble(i);

		}

		return doubleArray;

	}

}
