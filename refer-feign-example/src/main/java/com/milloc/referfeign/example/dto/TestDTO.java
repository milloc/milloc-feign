package com.milloc.referfeign.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author gongdeming
 * @create 2019-09-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
    private String path;
    private Map<String, Object> get;
    private Map<String, Object> post;
}
