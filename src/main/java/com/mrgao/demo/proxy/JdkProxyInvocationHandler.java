package com.mrgao.demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Mr.Gao
 * @date: 2022年05月25日 10:47
 * @description:
 */
public class JdkProxyInvocationHandler implements InvocationHandler {

    private Object target;

    public JdkProxyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("I Am JdkProxyInvocationHandler methodName: " + method.getName() + ", proxyObj:" + proxy.getClass().getSuperclass());
        Object invoke = method.invoke(target, args);
        return invoke;
    }

    /**
     * 创建代理类
     *
     * @param target
     * @param targetInterface
     * @param <T>
     * @return
     */
    public static <T> T createProxy(Object target, Class<T> targetInterface) {
        if (!targetInterface.isInterface()) {
            throw new RuntimeException("JDK代理必须满足是接口!");
        }
        if (!targetInterface.isAssignableFrom(target.getClass())) {
            throw new RuntimeException("目标target对象必须是接口的实现类!");
        }
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new JdkProxyInvocationHandler(target));
    }

}
