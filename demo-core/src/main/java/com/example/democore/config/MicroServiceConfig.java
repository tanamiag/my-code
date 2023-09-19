package com.example.democore.config;

import com.example.democore.msa.MsaProperties;
import com.example.democore.msa.annotations.MicroServiceEndpoint;
import com.example.democore.msa.config.MicroServiceEndpointBeanDefinitionRegistryPostProcessor;
import com.example.democore.msa.config.MicroServiceEndpointFactoryBean;
import com.example.democore.msa.proxy.DefaultMicroServiceEndpointClient;
import com.example.democore.msa.proxy.MicroServiceEndpointClient;
import com.example.democore.msa.proxy.ProxyController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import java.util.ArrayList;
import java.util.List;

@EnableConfigurationProperties(MsaProperties.class)
@Configuration
public class MicroServiceConfig implements EnvironmentAware{
	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;

	}

	/**
	 * 객체와 environment 정보를 Binding
	 *
	 * @param t Binding 대상 class type
	 * @param prefix environment 의 prefix
	 * @param environment Spring 에서 관리하는 환경정보 Bean
	 * @param <T> Binding 대상 타입
	 * @return Binding 완료된 객체
	 */
	public <T> T bindProperties(Class<T> t, String prefix, Environment environment) {

		T property =
				(T) Binder.get(environment)
						.bind(prefix, t)
						.orElse(null);

		return property;
	}

	@Bean
	public DefaultMicroServiceEndpointClient microServiceInterfaceClient() {
		MsaProperties msaProperties = bindProperties(MsaProperties.class, MsaProperties.PREFIX, environment);
		return new DefaultMicroServiceEndpointClient(msaProperties);
	}

	@Bean
	public MicroServiceEndpointBeanDefinitionRegistryPostProcessor microserviceInterfaceBeanDefinitionRegistryPostProcessor(MicroServiceEndpointClient microServiceEndpointClient) {
		List<String> basePackageList = new ArrayList<String>();
		basePackageList.add("com.example.demoserverendpoint");	// endpoint의 서비스 인터페이스 패키지 경로 스캔

		return new MicroServiceEndpointBeanDefinitionRegistryPostProcessor(
					basePackageList,
					MicroServiceEndpoint.class,
					MicroServiceEndpointFactoryBean.class,
				microServiceEndpointClient
				);
	}
	
//	@Bean
//	public ProxyController proxyController() {
//		return new ProxyController();
//	}

}

