package com.example.democore.msa.config;

import com.example.democore.msa.proxy.MicroServiceEndpointClient;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MicroServiceEndpointInvocationHandler implements InvocationHandler {
    private final MicroServiceEndpointClient microserviceEndpointClient;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return microserviceEndpointClient.request(method, args);
    }
}
