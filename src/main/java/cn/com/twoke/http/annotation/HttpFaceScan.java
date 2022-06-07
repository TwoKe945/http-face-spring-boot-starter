package cn.com.twoke.http.annotation;

import cn.com.twoke.http.HttpFaceScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>TODO</p>
 *
 * @author TwoKe
 * @since 2022/6/7 9:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HttpFaceScannerRegistrar.class})
public @interface HttpFaceScan {

    String[] value() default {};

    String[] basePackages() default {};

}
