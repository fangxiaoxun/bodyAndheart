package com.example.team_project.framkwork.core.mvc;

import com.example.team_project.framkwork.core.exception.BeanException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static final Map<String,Object> CACHE = new ConcurrentHashMap<>(16);
    private static final Map<String,Object> CONTROLLER_CACHE = new ConcurrentHashMap<>(16);

    /**
     * 由名字获取bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return getBean(beanName,null);
    }

    /**
     * 由名字获取bean，并返回beanClass类型的bean
     * @param beanName
     * @param beanClass
     * @return
     */
    public static  <T> T getBean(String beanName, Class<T> beanClass) {
        T sharedInstance = (T) CACHE.get(beanName);

        if (sharedInstance != null){
            return sharedInstance;
        }else {
            throw new BeanException("缓存中没有该对象!请确认您创建的对象是否被管理!");
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        T bean = null;
        for (Map.Entry<String, Object> entry : CACHE.entrySet()) {
            //找到接管的这个对象的父类
            Object value = entry.getValue();
            if (beanClass.isAssignableFrom(value.getClass())) {
                bean = (T) value;
                break;
            }
        }
        if (bean == null) {
            throw new BeanException("缓存中没有该对象!请确认您创建的对象是否被管理!");
        }
        return bean;
    }

    public static void chargeBean(String[] beanName,Object[] instances){
        if (beanName == null || instances == null) {

        }else if (beanName.length != instances.length){
            throw new BeanException("参数个数不匹配!无法进行匹配!");
        }else {
            for (int i = 0; i < instances.length; i++) {
                if (CACHE.containsKey(beanName[i])){
                    continue;
                }
                CACHE.putIfAbsent(beanName[i], instances[i]);
            }
        }
    }

    public static void chargeBean(String name, Object instance){
        if (name != null && !name.trim().equals("") && instance != null){
            if (!CACHE.containsKey(name)){
                CACHE.putIfAbsent(name,instance);
            }
        }
    }

    public static Object getController(String beanName) {
        return getController(beanName,Object.class);
    }

    public static <T> T getController(String beanName, Class<T> beanClass) {
        T shareController = (T) CONTROLLER_CACHE.get(beanName);

        if (shareController != null){
            return shareController;
        }else {
            throw new BeanException("缓存中没有该对象!请确认您创建的对象是否被管理!");
        }
    }

    public static <T> T getController(Class<T> beanClass) {
        T bean = null;
        for (Map.Entry<String, Object> entry : CONTROLLER_CACHE.entrySet()) {
            //找到接管的这个对象的父类
            Object value = entry.getValue();
            //只会找到第一个以传入的类为父类或同类的对象
            if (beanClass.isAssignableFrom(value.getClass())) {
                bean = (T) value;
                break;
            }
        }
        if (bean == null) {
            throw new BeanException("缓存中没有该对象!请确认您创建的对象是否被管理!");
        }
        return bean;
    }

    public static void chargeController(List<String> beanName, List<Object> instances) {
        if (beanName != null && instances != null) {
            if (beanName.size() != instances.size()) {
                throw new BeanException("controller的名字个数和要实例化的个数不匹配，管理失败!");
            }else {
                for (int i = 0; i < instances.size(); i++) {
                    //保证不会把上一个同beanName的覆盖
                    if (CONTROLLER_CACHE.containsKey(beanName.get(i))) {
                        System.out.println("接管的时候出现名字重复，故"+instances.get(i)+"没有被成功接管!");
                        continue;
                    }
                    CONTROLLER_CACHE.putIfAbsent(beanName.get(i),instances.get(i));
                }
            }
        }
    }

    public static void chargeController(String name, Object instance){
        if (name != null && !name.trim().equals("") && instance != null){
            if (!CONTROLLER_CACHE.containsKey(name)){
                CONTROLLER_CACHE.putIfAbsent(name,instance);
            }else {
                System.out.println("已经存在该名字的Controller，所以"+instance+"接管失败");
            }
        }
    }
}
