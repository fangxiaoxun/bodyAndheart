package com.example.team_project.framkwork.core.tranaction.proxyutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T getProxyInstance(Class<T> target, InvocationHandler h){
        return (T) Proxy.newProxyInstance(target.getClassLoader(), target.getInterfaces(), h);
    }
}
