package com.mrgao.demo.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: Mr.Gao
 * @date: 2022年05月25日 11:44
 * @description:
 */
public class CglibProxyMethodInterceptor implements MethodInterceptor {

    private Object target;

    public CglibProxyMethodInterceptor (Object target) {
        this.target = target;
    }

    /**
     * 方法拦截器
     *
     * @param proxy:代理对象
     * @param method:被代理的类的方法
     * @param args:参数
     * @param methodProxy:方法代理对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept (Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println ("====== CglibProxyMethodInterceptor :" + method + ", proxyObj:" + proxy.getClass ());
        // 调用被代理类的方法
        Object invoke = methodProxy.invokeSuper (target, args);
        return invoke;
    }


    /**
     * 创建代理
     *
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T createProxy (T target) {
        Enhancer enhancer = new Enhancer ();
        // 用来设置代理类的父类，即需要给哪个类创建代理类
        enhancer.setSuperclass (target.getClass ());
        // 设置回调
        enhancer.setCallback (new CglibProxyMethodInterceptor (target));
        return (T) enhancer.create ();
    }
}
