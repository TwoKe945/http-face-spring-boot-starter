package cn.com.twoke.http;

import cn.com.twoke.http.annotation.HttpFaceScan;
import cn.com.twoke.http.annotation.ServiceClient;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TwoKe
 */
public class HttpFaceScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(HttpFaceScan.class.getName()));

        if (attrs != null) {
            List<String> basePackages = new ArrayList<>();
            basePackages.addAll(Arrays.stream(attrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
            basePackages.addAll(Arrays.stream(attrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));

            //将接口转换为BeanDefinition对象放入spring中
            //CkClassPathScanner为自定义扫描类
            HttpFaceClassPathScanner classPathScanner = new HttpFaceClassPathScanner(beanDefinitionRegistry);
            classPathScanner.doScan(StringUtils.collectionToCommaDelimitedString(basePackages));
        }

    }

}
