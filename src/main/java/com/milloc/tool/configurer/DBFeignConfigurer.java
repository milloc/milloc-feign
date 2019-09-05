package com.milloc.tool.configurer;

import com.milloc.tool.client.DBClientClassPathScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.RestTemplate;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
//@Configuration
public class DBFeignConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate defaultTemplate() {
        return new RestTemplate();
    }

    @Configuration
    public static class DBFeignClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
        private ResourceLoader resourceLoader;
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            DBClientClassPathScanner scanner = new DBClientClassPathScanner(registry);
            scanner.setResourceLoader(resourceLoader);
            scanner.scan(AutoConfigurationPackages.get(this.beanFactory).toArray(new String[]{}));
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
