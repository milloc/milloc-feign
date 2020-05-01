package com.milloc.referfeign.springbootautoconfigure.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Objects;
import java.util.Set;

/**
 * ReferClient类扫描，并且生成beanDefinition
 *
 * @author gongdeming
 * @create 2019-09-05
 */
@Log4j2
public class ReferClientClassPathScanner extends ClassPathBeanDefinitionScanner {
    private final ReferClientFactoryBean<?> referClientFactoryBean = new ReferClientFactoryBean<>();

    public ReferClientClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            logger.debug("没有扫描到ReferClient");
        }
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            // 将接口转化成 referClientFactoryBean
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(beanDefinition.getBeanClassName()));
            beanDefinition.setBeanClass(this.referClientFactoryBean.getClass());
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
