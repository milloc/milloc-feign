package com.milloc.dbfeignspringbootautoconfigure.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * DBClient类扫描，并且生成beanDefinition
 *
 * @author gongdeming
 * @create 2019-09-05
 */
@Log4j2
public class DBClientClassPathScanner extends ClassPathBeanDefinitionScanner {
    private DBClientFactoryBean<?> dbClientFactoryBean = new DBClientFactoryBean<>();

    public DBClientClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            logger.debug("没有扫描到DBClient");
        }
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            // 将接口转化成 dbClientFactoryBean
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(this.dbClientFactoryBean.getClass());
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
