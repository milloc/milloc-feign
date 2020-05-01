package com.milloc.referfeign.springbootautoconfigure;

import com.milloc.referfeign.springbootautoconfigure.annotation.ReferClient;
import com.milloc.referfeign.springbootautoconfigure.client.ReferClientClassPathScanner;
import com.milloc.referfeign.springbootautoconfigure.parameter.ParameterResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
@Configuration
@AutoConfigureAfter({RestTemplateAutoConfiguration.class})
@Import({ReferFeignConfigurer.ReferFeignClientRegistrar.class})
@Log4j2
public class ReferFeignConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate defaultTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void init() {
        log.debug("ReferFeignConfigurer 自动配置启动");
    }

    public static class ReferFeignClientRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            // 扫描加载 ParameterResolver
            ClassPathBeanDefinitionScanner parameterResolverScanner = new ClassPathBeanDefinitionScanner(registry);
            parameterResolverScanner.resetFilters(false);
            parameterResolverScanner.addIncludeFilter(new AssignableTypeFilter(ParameterResolver.class));
            parameterResolverScanner.scan("com.milloc.referfeign.springbootautoconfigure.parameter");
            // 扫描 @ReferClient
            ReferClientClassPathScanner scanner = new ReferClientClassPathScanner(registry);
            scanner.resetFilters(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(ReferClient.class));
            scanner.scan(AutoConfigurationPackages.get(this.beanFactory).toArray(new String[]{}));
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
