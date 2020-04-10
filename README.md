# db-feign

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
2. 使用 ``@DBClient`` 标记远程调用服务
    ```
   @DBClient
   @RequestMapping(value = "http://localhost:8000", method = RequestMethod.POST)
   public interface TestClient {
       @RequestMapping(path = "/{a}/{b}", method = {RequestMethod.GET})
       Map<String, Object> testGet(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);
   
       @RequestMapping(path = "/{a}/{b}", method = {RequestMethod.POST})
       TestDTO testPost(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);
   
       @RequestMapping("/bcdd")
       Map<String, Object> test(@RequestBody String abc, @RequestParam("hello") String hello);
   }
    ```

### 源码入口
``com.milloc.dbfeignspringbootautoconfigure.DBFeignConfigurer``
