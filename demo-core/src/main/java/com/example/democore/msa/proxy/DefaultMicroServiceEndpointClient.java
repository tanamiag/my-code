package com.example.democore.msa.proxy;

import com.example.democore.msa.MsaProperties;
import com.example.democore.msa.annotations.MicroServiceEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultMicroServiceEndpointClient implements MicroServiceEndpointClient {
	private RestTemplate restTemplate = new RestTemplate();

	private final MsaProperties msaProperties;
	
	private HttpHeaders getByPassHeader(){
		// 브라우저로부터 넘겨받은 요청 헤더 set
		final ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest r =  sra.getRequest();

    	Enumeration<String> hn = r.getHeaderNames();

		// yml에 지정한 헤더만 endpoint로 넘겨준다.
    	Set<String> byPassHeaderName = msaProperties.getByPassHeader();
    	
    	HttpHeaders header = new HttpHeaders();
    	while(hn.hasMoreElements()) {
    		String headerName = hn.nextElement();
    		if(!byPassHeaderName.contains(headerName.toLowerCase())){
    			continue;
    		}
    		
    		Enumeration<String> headerValues = r.getHeaders(headerName);
    		while(headerValues.hasMoreElements()) {
    			header.add(headerName, headerValues.nextElement());
    		}
    	}
    	
    	return header;
	}

	@Override
	public Object request(Method method, Object[] args) {
        final MicroServiceEndpoint annotation = method.getDeclaringClass().getAnnotation(MicroServiceEndpoint.class);	// 서비스 인터페이스의 어노테이션
        final String annotationValue = annotation.value();	// 어노테이션 value의 서버 식별값
        final String baseUrl = msaProperties.getProxyUri().get(annotationValue);	// annotationValue으로 yml에서 URL을 가져와야함
        final String className = method.getDeclaringClass().getName();	// 서비스 인터페이스명
        final String methodName = method.getName();	// 메소드명
        final String[] argumentClassList = Arrays.stream(method.getParameterTypes()).map(each -> each.getName()).toArray(String[]::new);	// 파라미터 타입명 리스트
		
        JsonRpcRequest request = new JsonRpcRequest();
        JsonRpcRequest.Params params = new JsonRpcRequest.Params();
        params.setClassName(className);
        params.setMethodName(methodName);
        params.setArgumentClassList(argumentClassList);
        
        if(args!=null) {
            final String[] arguStringList = new String[args.length];	// 파라미터 값 list
    		for (int i=0; i<args.length; i++) {
    			Object arguObject = args[i];

    			ObjectMapper objectMapper = new ObjectMapper();
    			String argu = null;
    	        try {
    	        	argu = objectMapper.writeValueAsString(arguObject);
    	        } catch (JsonProcessingException e) {
    	            e.printStackTrace();
    	        }
    	        arguStringList[i] = argu;
    		}
    		
    		params.setArgumentList(arguStringList);
        }

        request.setParams(params);

        final HttpHeaders header = getByPassHeader();

        RequestEntity<JsonRpcRequest> entity = null;
        try {
			entity = new RequestEntity<>(request, header, HttpMethod.POST, new URI(baseUrl));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // From ProxyController
        ResponseEntity<JsonRpcResponse> response = restTemplate.exchange(entity, JsonRpcResponse.class);
        String resultString = response.getBody().getResult(); // 역직렬화 : response (응답 json String) -> ex. List<String>
		ObjectMapper objectMapper = new ObjectMapper();
		Object result = null;
    	// 역직렬화 : 파라미터 값(json 형식의 String)을 파라미터 타입(Class)의 객체로 생성
    	try {
			result = objectMapper.readValue(resultString, method.getReturnType());
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

}
