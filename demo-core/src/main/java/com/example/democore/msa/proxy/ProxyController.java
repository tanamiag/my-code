package com.example.democore.msa.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
// client 실행 시 해당 클래스가 빈으로 등록 되려고 할 때,
// client의 yml에 해당 프로퍼티가 없어 @RequestMapping 에서 에러 발생
// -> 프로퍼티가 없다면 빈으로 등록되지 않게 처리
@ConditionalOnExpression("'${msa-endpoint-uri.rpc.endpoint:false}' != 'false'")
public class ProxyController {
	@Autowired
	private ApplicationContext ac;

	@RequestMapping(value = "${msa-endpoint-uri.rpc.endpoint}", method = RequestMethod.POST)
	public JsonRpcResponse request(@RequestBody JsonRpcRequest request) {
		final JsonRpcRequest.Params params = request.getParams();

		Object[] paramObjects = null;
		
		if (params.getArgumentClassList() != null && params.getArgumentClassList().length !=0) {
			paramObjects = buildParamObject(params);	// 요청 파라미터 역직렬화 (= 해당 타입의 객체로 생성)	
		}
		
		Object bean = discoveryBean(params);	// 컴포넌트 추출
		Method method = discoveryMethod(bean, params);

		JsonRpcResponse jsonRpcResponse = null;
		try {
			Object resultObject = method.invoke(bean, paramObjects);	// 메소드 실행

			ObjectMapper objectMapper = new ObjectMapper();
			String result = objectMapper.writeValueAsString(resultObject);	// 결과 직렬화 (Object -> String)
			jsonRpcResponse = new JsonRpcResponse();
			jsonRpcResponse.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonRpcResponse;
	}


	private Object[] buildParamObject(JsonRpcRequest.Params params) {

		final String[] parameterClassNames = params.getArgumentClassList();
		// Class list 추출
		final List<Class<?>> paramClasses = Stream.of(parameterClassNames).map(each -> {
			try {
				return ac.getClassLoader().loadClass(each);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());

		// 파라미터 값 list
		final String[] arguStringList = params.getArgumentList();

		final Object[] arguList = new Object[arguStringList.length];
		for (int i=0; i<arguStringList.length; i++) {
			Class<?> arguClass = paramClasses.get(i);
			String arguString = arguStringList[i];	// json 형식의 String

			ObjectMapper objectMapper = new ObjectMapper();
			Object argu = null;
	        try {
	        	// 역직렬화 : 파라미터 값(json 형식의 String)을 파라미터 타입(Class)의 객체로 생성
	        	argu = objectMapper.readValue(arguString, arguClass);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
			arguList[i] = argu;
		}

		return arguList;
	}

	private Method discoveryMethod(Object bean, JsonRpcRequest.Params params) {

		final String methodName = params.getMethodName();
		final String[] parameterClassNames = (params.getArgumentClassList() != null ? params.getArgumentClassList() : new String[0] );
		
		final List<Class<?>> paramClasses = Stream.of(parameterClassNames).map(each -> {
			Class<?> result  = null;
			try {
				result = ac.getClassLoader().loadClass(each);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return result;
		}).collect(Collectors.toList());
		
		
		final String className = bean.getClass().getName();
		Class<?> clazz = null;
		try {
			clazz = ac.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Method[] methods = clazz.getDeclaredMethods();

		Method targetMethod = null;
		for(Method each : methods) {
			String name = each.getName();
			if (!name.equals(methodName)) {
				continue;
			}

			if (each.getParameterCount() != parameterClassNames.length){
				continue;
			}

			List<Class<?>> methodParamTypeList = Stream.of(each.getParameterTypes()).collect(Collectors.toList());
			if (!ListUtils.isEqualList(methodParamTypeList, paramClasses)) {
				continue;
			}

			targetMethod = each;
			break;
		}

		if (targetMethod == null) {
			// 404 응답
			throw new RuntimeException();
		}

		return targetMethod;
	}

	private Object discoveryBean(JsonRpcRequest.Params params){
		final String className = params.getClassName();
		Class<?> clazz = null;
		Object bean = null;
		try {
			clazz = ac.getClassLoader().loadClass(className);
			bean = ac.getBean(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return bean;
	}



}
