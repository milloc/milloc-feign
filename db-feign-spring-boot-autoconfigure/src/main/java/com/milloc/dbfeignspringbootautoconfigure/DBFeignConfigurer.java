package com.milloc.dbfeignspringbootautoconfigure;

import com.milloc.dbfeignspringbootautoconfigure.annotation.DBClient;
import com.milloc.dbfeignspringbootautoconfigure.client.DBClientClassPathScanner;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
@Configuration
@AutoConfigureAfter({RestTemplateAutoConfiguration.class})
@Import({DBFeignConfigurer.DBFeignClientRegistrar.class})
@Log4j2
public class DBFeignConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate defaultTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void init() {
        log.debug("DBFeignConfigurer 自动配置启动");
    }

    public static class DBFeignClientRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            DBClientClassPathScanner scanner = new DBClientClassPathScanner(registry);
            scanner.resetFilters(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(DBClient.class));
            scanner.scan(AutoConfigurationPackages.get(this.beanFactory).toArray(new String[]{}));
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
