package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;

import java.lang.reflect.Parameter;

/**
 * 参数解析器，用于解析接口方法的参数，生成请求
 *
 * @author milloc
 * @date 2020-05-03
 */
public interface ParameterResolver {
    /**
     * 解析参数
     *
     * @param builder 请求构建器
     * @param param   当前参数
     * @param value   当前参数值
     * @return 是否解析成功
     */
    boolean resolved(RequestBuilder builder, Parameter param, Object value);
}
