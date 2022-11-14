package com.example.team_project.framkwork.utils;

import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.core.annotation.Controller;
import com.example.team_project.framkwork.core.annotation.RequestMapping;
import com.example.team_project.framkwork.core.inject.Dependency;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.core.mvc.ControllerInfo;
import com.example.team_project.framkwork.core.mvc.ControllerRequestMappingInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 搜索提供的包名下有没有使用Bean注解的类或有返回值的方法，若有，则把他们给BeanFactory管理
 */
public class BeanScan {
    private static final int DEFAULT_CAPACITY = 16;

    private static final Map<Bean, Class<?>> BEAN_INJECT = new HashMap<>(DEFAULT_CAPACITY);
    private static final Map<Class<?>,Object> BEAN_INSTANCE_INJECT = new HashMap<>(DEFAULT_CAPACITY);
    private static final Map<Controller, Class<?>> CONTROLLER_INJECT = new HashMap<>(DEFAULT_CAPACITY);
    private static final Map<Class<?>,Object> CONTROLLER_INSTANCE_INJECT = new HashMap<>(DEFAULT_CAPACITY);

    /**
     * 对包下有@Bean注解的类进行依赖管理
     * @param packetPath 要扫描的包
     */
    public static void scan(String packetPath) {
        //获取包下所有类的类路径
        List<String> classPaths = getClassPathList(packetPath);
        if (classPaths != null){
            //使得对象的名字个数与将要创建的对象个数一致，逐一对应
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Object> instances = new ArrayList<>();
            //开始创建对象的工作
            for (int i = 0; i < classPaths.size(); i++) {
                try {
                    Class<?> clazz = Class.forName(classPaths.get(i));
                    //判断该类需不需要被管理
                    if (clazz.isAnnotationPresent(Bean.class)) {
                        Bean bean = clazz.getAnnotation(Bean.class);
                        //需要被管理则把类放进map集合中，方便读取顺序来实例化
                        BEAN_INJECT.put(bean,clazz);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                instances(names, instances,"normal");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //把对象交给Bean容器管理
            BeanFactory.chargeBean(names.toArray(new String[0]), instances.toArray());
            //把需要管理的对象先进行依赖注入
            for (Map.Entry<Class<?>, Object> entry : BEAN_INSTANCE_INJECT.entrySet()) {
                try {
                    Dependency.autoWire(entry.getKey(),entry.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对包下有@Controller注解的类进行依赖管理
     * @param packetPath 要扫描的包
     */
    public static void controllerScan(String packetPath, List<ControllerInfo<?>> controllerInfos) {
        List<String> classPaths = getClassPathList(packetPath);
        if (classPaths != null) {
            List<String> names = new ArrayList<>();
            List<Object> instances = new ArrayList<>();
            //开始创建对象的工作
            Class<Controller> controller = Controller.class;
            for (String classPath : classPaths) {
                try {
                    Class<?> clazz = Class.forName(classPath);
                    if (clazz.isAnnotationPresent(controller)) {
                        Controller annotation = clazz.getAnnotation(controller);
                        //把注解和类对象放入controller依赖容器，调用方法给names和instances赋值
                        CONTROLLER_INJECT.put(annotation,clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                //对names和instances进行赋值
                instances(names,instances,"mvc");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //对Controller进行管理
            BeanFactory.chargeController(names,instances);
            //实现Controller中的成员变量赋值
            for (Map.Entry<Class<?>, Object> entry : CONTROLLER_INSTANCE_INJECT.entrySet()) {
                try {
                    //依据类型进行依赖注入
                    Dependency.autoWire(entry.getKey(),entry.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //把Controller的信息加到controllerInfos

        }
    }

    private <T> void initControllerInfos(List<ControllerInfo<?>> controllerInfos) {

        Class<RequestMapping> requestMapping = RequestMapping.class;
        for (Map.Entry<Controller, Class<?>> entry : CONTROLLER_INJECT.entrySet()) {
            ControllerInfo<T> controllerInfo;
            Class<?> clazz = entry.getValue();
            //当Controller没有在类上设置@RequestMapping时，信息为null
            ControllerRequestMappingInfo requestMappingInfo = null;
            //设置Controller本身的信息，可能没有
            if (clazz.isAnnotationPresent(requestMapping)) {
                RequestMapping annotation = clazz.getAnnotation(requestMapping);
                String[] urlPattern = getUrlPatternByAnnotation(annotation);
                requestMappingInfo = new ControllerRequestMappingInfo(annotation.value(),urlPattern,
                        annotation.priority(),annotation.type());

            }
            //查看方法中具有的requestMappingInfo注解信息,并添加到集合中
            List<ControllerRequestMappingInfo> methodMappingInfos = new LinkedList<>();
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(requestMapping)) {
                    continue;
                }
                RequestMapping mapping = method.getAnnotation(requestMapping);
                String[] methodPattern = getUrlPatternByAnnotation(mapping);
                //把Controller中方法的相关信息进行封装
                ControllerRequestMappingInfo methodInfo = new ControllerRequestMappingInfo(
                        mapping.value(),methodPattern,mapping.priority(),mapping.type(),method
                );
                //把method的相关信息添加到集合中
                methodMappingInfos.add(methodInfo);

            }
            //实例化controllerInfo，保存controller的信息
            controllerInfo = new ControllerInfo<T>(entry.getKey().value(), (Class<T>) entry.getValue(),
                    requestMappingInfo,methodMappingInfos);
        }
    }

    /**
     * 获取Controller的访问路径
     * @param annotation 对应的RequestMapping注解
     * @return 访问路径
     */
    private String[] getUrlPatternByAnnotation(RequestMapping annotation) {
        String[] urlPattern = annotation.urlPattern();
        if (urlPattern.length == 0) {
            String value = annotation.value();
            if ("".equals(value.trim())) {
                urlPattern = new String[]{};
            }else {
                //若没有设置urlPattern,则为value的路径
                urlPattern = new String[]{value};
            }
        }
        if (urlPattern.length == 0) {
            throw new RuntimeException("没有设置访问的url");
        }
        return urlPattern;
    }

    /**
     * 按照顺序进行实例化操作
     * @param names 用于存储bean的名字，便于调用charge方法
     * @param instances 用于存储bean的实例，便于调用charge方法
     * @throws Exception
     */
    private static void instances(List<String> names, List<Object> instances,String vesselType) throws Exception {
        if ("mvc".equals(vesselType)) {
            //遍历需要存放需要管理的Controller信息
            for (Map.Entry<Controller, Class<?>> entry : CONTROLLER_INJECT.entrySet()) {
                Controller controller = entry.getKey();
                Class<?> clazz = entry.getValue();
                Object obj = clazz.getDeclaredConstructor().newInstance();
                //把该Controller的名字放入名字的集合中，便于调用容器管理方法
                names.add(controller.value());
                //把该Controller的实例化对象放入集合中，便于调用容器管理方法
                instances.add(obj);
                CONTROLLER_INSTANCE_INJECT.put(clazz,obj);
            }
        }else {
            for (Map.Entry<Bean, Class<?>> entry : BEAN_INJECT.entrySet()) {
                Bean bean = entry.getKey();
                Class<?> clazz = entry.getValue();
                Object obj = clazz.getDeclaredConstructor().newInstance();
                names.add(bean.value());
                instances.add(obj);
                BEAN_INSTANCE_INJECT.put(clazz, obj);
            }
        }
    }

    /**
     * 获得class文件的类路径
     * @param file 非文件夹的文件
     * @return 类路径
     */
    private static String getClassPath(File file) {
        //获取绝对路径
        String path = file.getAbsolutePath();
        //判断是否为class文件，是则进行转换操作
        if (path.endsWith(".class")) {
            path = path.substring(8 + path.lastIndexOf("classes"), path.lastIndexOf(".class")).trim();
            //不太懂为什么一定要\\\\才能表示\
            path = path.replaceAll("\\\\",".");
            return path;
        }
        //否则返回null
        return null;
    }

    /**
     * 通过包，来获取包下所有类的类路径
     * @param packetPath 要扫描的包
     * @return 所有类的类路径集合，若包不存在，则返回的为null
     */
    private static List<String> getClassPathList(String packetPath) {
        //通过类加载器获取packetPath下的资源
        URL resource = BeanScan.class.getClassLoader().getResource(packetPath);
        if (resource != null && resource.toString().startsWith("file:")) {
            //获取路径下所有的文件，包括class文件
            List<File> files = getAllFile(new File(resource.getFile()));
            //存放所有的类路径，用链表，可以避免因为不好判断大小，使得arrayList总是扩容
            List<String> classPaths = new LinkedList<>();
            //遍历file，通过file获取绝对路径，再转化为类路径，便于反射
            for (File file : files) {
                String classPath = getClassPath(file);
                if (classPath != null && !classPath.equals("")) {
                    classPaths.add(classPath);
                }
            }
            return classPaths;
        }
        //路径有误，返回null
        return null;
    }

    /**
     * 获取某个包下所有的文件
     * @param packet
     * @return
     */
    private static List<File> getAllFile(File packet) {
        List<File> fileList = new ArrayList<>(16);
        getAllByPacketFile(packet, fileList);
        return fileList;
    }

    /**
     * 递归方法获取文件夹中的所有非文件夹的文件
     *
     * @param dir      文件
     * @param fileList 文件存入的list
     */
    private static void getAllByPacketFile(File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                getAllByPacketFile(file, fileList);
            }
        } else {
            fileList.add(dir);
        }
    }
}
