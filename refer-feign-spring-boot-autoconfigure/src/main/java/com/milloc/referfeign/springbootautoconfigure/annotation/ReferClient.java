package com.milloc.referfeign.springbootautoconfigure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置refer
 *
 * <p>
 * 配合以下注解，可以生成请求。对于没有注解的属性，会自动解析为请求体，当且仅当请求体不存再的时候。
 * 对于文件，必须转换成{@link org.springframework.web.multipart.MultipartFile MultipartFile}。
 *
 * <table>
 *     <tr>
 *         <th>注解</th>
 *         <th>位置</th>
 *         <th>作用</th>
 *     </tr>
 *     <tr>
 *         <td>{@link org.springframework.web.bind.annotation.RequestMapping RequestMapping}</td>
 *         <td>接口，方法</td>
 *         <td>标记请求的基本信息，比如 {@code path(), method(), header(), consumes()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.springframework.web.bind.annotation.PathVariable PathVariable}</td>
 *         <td>参数</td>
 *         <td>表示url上的一个path变量, 可以用在{@code Map<String, String>}和{@code POJO}上</td>
 *     </tr>
 *     <tr>
 *         <td>{@link QueryParam QueryParam}</td>
 *         <td>参数</td>
 *         <td>表示url上的一个query参数，可以是{@code array, POJO, Map<String, ?>}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.springframework.web.bind.annotation.RequestHeader RequestHeader}</td>
 *         <td>参数</td>
 *         <td>表示header，可以是{@code String, List<String>, Map<String, String>}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.springframework.web.bind.annotation.RequestBody RequestBody}</td>
 *         <td>参数</td>
 *         <td>表示请求体，可以是{@code POJO, Map}，只有最后一个生效</td>
 *     </tr>
 * </table>
 *
 * @author milloc
 * @date 2020-05-03
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferClient {
}
