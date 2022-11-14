package com.example.team_project.framkwork.core.mvc;

import com.example.team_project.framkwork.core.annotation.ComponentScan;
import com.example.team_project.framkwork.core.annotation.Configuration;
import com.example.team_project.framkwork.core.annotation.MethodReturn;
import com.example.team_project.framkwork.core.exception.LoadConfigException;
import com.example.team_project.framkwork.utils.BeanScan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>加载配置,并且将配置类中用MethodReturn的方法和ComponentScan注解填的包下有bean注解的全部交给容器接管</p>
 * <p>推荐先管理普通容器，不然无法进行Controller的依赖注入</p>
 */
public class LoadConfig {

    private static List<ControllerInfo<?>> controllerInfos = null;
    /**
     * 加载配置
     * @param config 配置类
     */
    public static void load(Class<?> config) {
        //只有传入参数非null才能继续进行
        if (config != null){
            //必须有Configuration注解才可以加载
            Class<Configuration> configurationClass = Configuration.class;
            if (!config.isAnnotationPresent(configurationClass)){
                throw new LoadConfigException("加载的类不是一个配置类");
            }
            //获取在Configuration类上的注解
            Configuration configAnnotation = config.getAnnotation(configurationClass);
            //若加载的不是mvc配置，则加载默认的普通容器
            if (!"mvc".equals(configAnnotation.value())) {
                loadDefault(config);
            }else {
                controllerInfos = new LinkedList<>();
                //加载mvc容器,并把加载进去的Controller的信息也存入到controllerInfos集合中
                mvcLoad(config,controllerInfos);
            }
        }
    }

    /**
     * 按照给的配置类和注解类，对配置类中所具有的方法中带MethodReturn注解的方法存储到集合中，并且根据MethodReturn的order值来进行升序排序
     * @return 升序排序后的具有@MethodReturn的方法
     */
    private static ArrayList<Method> getSoredMethod(Class<?> config, Class<MethodReturn> methodReturn){
        //获取配置类中所有的方法
        Method[] methods = config.getDeclaredMethods();
        ArrayList<Method> hasMethodReturn = new ArrayList<>();

        for (Method method : methods) {
            //判断方法是否有MethodReturn注解
            if (method.isAnnotationPresent(methodReturn)){
                hasMethodReturn.add(method);
            }
        }
        //排序,按照注解上的methodOrder的数字大小进行排序，解决部分依赖问题
        hasMethodReturn.sort(Comparator.comparingInt(o -> o.getAnnotation(methodReturn).methodOrder()));
        return hasMethodReturn;
    }

    /**
     * 默认的加载方式，当加载的配置并非mvc配置时，采取该加载方式
     * @param config
     */
    private static void loadDefault(Class<?> config) {
        Class<MethodReturn> methodReturn = MethodReturn.class;
        MethodReturn annotation = null;
        ArrayList<Method> hasMethodReturn = getSoredMethod(config, methodReturn);
        for (Method method : hasMethodReturn) {
            //获取方法上的MethodReturn注解对象
            annotation = method.getAnnotation(methodReturn);
            //获取方法的参数个数
            int parameterCount = method.getParameterCount();
            //若无参数则可以直接运行方法
            if (parameterCount == 0){
                try {
                    Object value = method.invoke(config.getConstructor().newInstance());
                    if (value != null) {
                        //创建实例来调用方法
                        BeanFactory.chargeBean(annotation.value(), value);
                    }else {
                        System.out.println("方法运行完，获取到的值为null,管理失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("管理失败!");
                }
            }else {
                //获取参数类型
                Class<?>[] types = method.getParameterTypes();
                //存入参数（参数需被管理）
                Object[] params = new Object[parameterCount];
                for (int i = 0; i < types.length; i++) {
                    //获取参数对象
                    params[i] = BeanFactory.getBean(types[i]);
                }
                try {
                    Object value = method.invoke(config.getConstructor().newInstance(), params);
                    //创建实例来调用方法
                    if (value != null) {
                        //创建实例来调用方法
                        BeanFactory.chargeBean(annotation.value(), value);
                    }else {
                        System.out.println("方法运行完，获取到的值为null,管理失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("管理失败!");
                }
            }
        }
        //在管理完配置类方法的返回值后，进行对包中需要管理的类进行管理
        Class<ComponentScan> clazz = ComponentScan.class;
        if (config.isAnnotationPresent(clazz)){
            //管理所有有Bean注解的对象
            ComponentScan scan = config.getAnnotation(clazz);
            BeanScan.scan(scan.packetPath());
        }
    }

    private static void mvcLoad(Class<?> mvcConfig, List<ControllerInfo<?>> controllerInfos) {
        Class<ComponentScan> clazz = ComponentScan.class;
        if (mvcConfig.isAnnotationPresent(clazz)){
            //管理所有有Controller注解的对象
            ComponentScan scan = mvcConfig.getAnnotation(clazz);
            BeanScan.controllerScan(scan.packetPath(), controllerInfos);
        }
    }

    protected List<ControllerInfo<?>> getControllerInfos() {
        if (controllerInfos != null) {
            return controllerInfos;
        }else {
            throw new LoadConfigException("mvc容器没有被加载过");
        }
    }
}
