package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;

import java.lang.reflect.Parameter;

public interface ParameterResolver {
    boolean resolved(RequestBuilder builder, Parameter param, Object value);
}
