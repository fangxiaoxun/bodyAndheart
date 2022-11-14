package com.example.team_project.framkwork.utils;

import com.example.team_project.framkwork.jdbc.annotation.Param;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *  Map集合使用的工具包
 */
public class MapUtils {
    /**
     * 由参数map创建target的实例化对象
     * @param target 目标类
     * @param params 参数map集合
     * @return 要创建的实例对象,当出现没有参数时,会返回null,当有异常时候,返回null
     */
    public static  <T> T instanceByMap(Class<T> target, Map<String,Object> params){
        T obj = null;
        //若参数的长度为0，则代表不用实例化这个类
        if (params.size() != 0) {
            try {
                obj = target.getConstructor().newInstance();
                //找到每一个成员变量
                Field[] fields = target.getDeclaredFields();
                Class<Param> paramClass = Param.class;
                for (Field field : fields) {
                    //若成员变量使用了Param注解，则进行赋值操作
                    //暴力赋值，若要改动，推荐改为非暴力赋值
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(paramClass)) {
                        Param param = field.getAnnotation(paramClass);
                        field.set(obj, params.get(param.value()));
                    } else {
                        //若没有带@Param注解，则根据变量名进行赋值
                        field.set(obj, params.get(field.getName()));
                    }
                }
            } catch (Exception e) {
                obj = null;
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static Map<String,Object> getMap(String key, Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return map;
    }

    /**
     * bean转换为Map集合，暴力获取成员变量
     * @param bean 要转换的bean对象
     * @return bean的成员数据的转换成为map集合
     */
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> params = new HashMap<>();
        //暴力获取所有的成员变量
        Field[] fields = bean.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                //获取变量值
                Object value = field.get(bean);
                //如果值为null，则不添加进集合
                if (value != null) {
                    params.put(field.getName(), value);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

}
