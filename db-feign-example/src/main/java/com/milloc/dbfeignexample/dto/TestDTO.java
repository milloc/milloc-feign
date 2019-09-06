package com.milloc.dbfeignexample.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author gongdeming
 * @create 2019-09-06
 */
@Data
public class TestDTO {
    private String path;
    private Map<String, Object> get;
    private Map<String, Object> post;
}
