package cn.com.twoke.http;

import cn.com.twoke.http.annotation.ServiceClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * <p>TODO</p>
 *
 * @author TwoKe
 * @since 2022/6/7 9:10
 */
public class HttpFaceClassPathScanner extends ClassPathBeanDefinitionScanner {

    public HttpFaceClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // 增加过滤，为接口类，并且接口上包含ServiceClient注解
        return beanDefinition.getMetadata().isInterface() &&
                beanDefinition.getMetadata().isIndependent() &&
                beanDefinition.getMetadata().hasAnnotation(ServiceClient.class.getName());
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            System.out.println("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface. Bean already defined with the same name!");
            return false;
        }
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // spring默认不会扫描接口，此处设置为true，不做过滤
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            System.out.println("未扫描到有ServiceClient注解接口");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        // 此段作用,将所有带CkInterfaceAnnotation注解接口,定义成beanDefinition对象
        // beanDefinitions中的bean对象指向接口的代理类
        // 在使用@Autowired注解注入接口时,其实注入的是接口代理对象
        beanDefinitions.forEach((BeanDefinitionHolder holder) -> {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            // 设置CkFactoryBean构造方法参数
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(HttpFaceFactoryBean.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        });
    }
}
