package com.example.democore.msa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class MicroServiceEndpointFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> beanClass;

    private final InvocationHandler invocationHandler;

    @Override
    public T getObject() throws Exception {
        // 인터페이스 기반으로 JDK 동적 프록시 객체 생성
        // 같은 invocationHandler 로직을 사용하여 공통화 함
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { this.beanClass }, invocationHandler);
    }

    @Override
    public Class<T> getObjectType() {
        return this.beanClass;
    }
}
