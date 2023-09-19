package com.example.democore.msa.proxy;

import java.lang.reflect.Method;

public interface MicroServiceEndpointClient {
	Object request(Method method, Object[] args);
}
