package cn.com.twoke.http;

import cn.com.twoke.http.creator.FaceCreator;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>TODO</p>
 *
 * @author TwoKe
 * @since 2022/6/7 9:12
 */
public class HttpFaceFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;

    public HttpFaceFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        return FaceCreator.getFace(clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
