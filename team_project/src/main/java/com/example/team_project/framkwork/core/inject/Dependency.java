package com.example.team_project.framkwork.core.inject;

import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.mvc.BeanFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 引用类型的依赖管理
 */
public class Dependency {
    /**
     * 传入对象和对应的类，进行引用类型的依赖注入
     * @param clazz 对应的类
     * @param instance 实例对象
     * @throws IllegalAccessException
     */
    public static void autoWire(Class<?> clazz,Object instance) throws IllegalAccessException {
        //暴力反射获取所有成员变量(父类也要)
        List<Field> fields = allFields(clazz);
        Class<AutoWire> autoWire = AutoWire.class;
        for (Field field : fields) {
            //查看成员变量是否带有@AutoWire注解
            if (field.isAnnotationPresent(autoWire)){
                AutoWire auto = field.getAnnotation(autoWire);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                //如果名字为默认值，则进行按类型注入
                if ("".equals(auto.value())) {
                    field.set(instance,BeanFactory.getBean(fieldType));
                }else {
                    //如果名字不为默认值，则进行按名字和类型进行注入
                    field.set(instance, BeanFactory.getBean(auto.value(),fieldType));
                }
            }
        }
    }

    public static void autoWire(Class<?> clazz) throws Exception {
        Object instance = clazz.getConstructor().newInstance();
        autoWire(clazz,instance);
    }

    private static List<Field> allFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>();
        while (clazz != null) {
            //把类的所有成员放入链表
            fields.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            //找到该类的父类，便于把父类成员也放入链表
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
