package com.example.democore.msa.config;

import com.example.democore.msa.proxy.MicroServiceEndpointClient;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MicroServiceEndpointBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private final List<String> basePackages;

    private final Class<? extends Annotation> annotationClass;

    // MicroServiceEndpointFactoryBean.class,
    private final Class<?> factoryBeanClass;

    private final MicroServiceEndpointClient microserviceEndpointClient;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    // 각 서비스 인터페이스에 대한 BeanDefinition 생성
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        final List<Class<?>> classList = scanServiceInterfaceList(registry);
        for (Class<?> each : classList) {
            // 인터페이스 기반으로 JDK 동적 프록시 빈 생성되도록 BeanDefinition 생성
            final BeanDefinition bd = createBeanDefinition(each);

            // FactoryBean을 구현한 클래스의 Object를 빈으로 등록할 때 이름을 생성하고자 하는 빈의 이름으로 지정해주어야 한다
            registry.registerBeanDefinition(each.getName(), bd);
        }
    }

    private BeanDefinition createBeanDefinition(Class<?> serviceInterfaceClass) {

        // BeanDefinition 메타 정보를 기반으로 스프링 빈 생성 예정
        // FactoryBean생성자가 가변적이므로 동적 빈 등록 위해 BeanDefinition 사용
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        // * 특정 인터페이스에 대한 FactoryBean을 BeanDefinition으로 등록
        // (해당 FactoryBean이 beanDefinition에 set 됨 으로써 빈으로 등록 됨)
        // * FactoryBean이기 때문에 getObject() 기반으로도 빈(= JDK 동적 프록시 객체) 생성 됨
        beanDefinition.setBeanClass(factoryBeanClass);

        // MicroServiceEndpointFactoryBean 클래스의 생성자 설정하여 필드(beanClass, invocationHandler) 셋팅
        final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, serviceInterfaceClass);
        constructorArgumentValues.addIndexedArgumentValue(1, new MicroServiceEndpointInvocationHandler(microserviceEndpointClient));
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);

        return beanDefinition;
    }

    private List<Class<?>> scanServiceInterfaceList(BeanDefinitionRegistry registry) {
        final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;

        // @MicroServiceEndpoint 가 붙은 모든 인터페이스 목록 스캔
        final List<Class<?>> l = basePackages.stream()
                                             .map(each -> new Reflections(each).getTypesAnnotatedWith(annotationClass))
                                             .flatMap(each -> each.stream())
                                             .filter(each -> each.isInterface())
                                             .distinct().collect(Collectors.toList());

        // 각 인터페이스 중에서 구현제가 없는 인터페이스만 추출 (BeanFactory에 구현체인 Bean이 없어야함)
        return l.stream()
                .filter(each -> beanFactory.getBeanNamesForType(each).length <= 0)
                .collect(Collectors.toList());
    }
}
