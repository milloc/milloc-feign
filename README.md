# refer-feign

#### 介绍
自己写的类似于open-feign的远程调用，基于RestTemplate调用。
参考了Mybatis的自动注册代码

### 目的
1. 了解spring-boot-starter怎么工作
2. 了解怎么扫描注入Bean

### 原理
1. Proxy 动态代理生成远程调用服务
2. RestTemplate 实现远程调用

### 使用
1. 引入依赖
    ```
    <dependency>
        <groupId>com.milloc</groupId>
        <artifactId>db-feign-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```
2. 使用 ``@ReferClient`` 标记远程调用服务
    ```
   @ReferClient
   public interface TestClient {
       @PostMapping(path = "http://localhost/{a}/{b}")
       String post(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, @QueryParam String ccc);
   
       @GetMapping(path = "http://localhost/{a}/{b}")
       String get(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, String ccc);
   
       @PostMapping(value = "http://localhost/bcdd", consumes = MediaType.APPLICATION_JSON_VALUE)
       String json(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);
   
       @PostMapping(value = "http://localhost/bcdd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
       String form(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);
   }
    ```

### 源码入口
``com.milloc.referfeign.springbootautoconfigure.ReferFeignConfigurer``
