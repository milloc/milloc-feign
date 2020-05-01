package com.milloc.referfeign.springbootautoconfigure.parameter;

import org.springframework.core.Ordered;

import java.lang.reflect.Parameter;

public interface ParameterResolver extends Ordered {
    boolean resolved(RequestBuilder builder, Parameter param, Object value);
}
