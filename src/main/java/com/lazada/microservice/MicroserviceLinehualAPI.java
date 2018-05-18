package com.lazada.microservice;

import com.lazada.microservice.constans.BasicResult;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootConfiguration
@EnableScheduling // 启用任务
@ComponentScan(basePackages = "com.lazada.microservice")
@SpringBootApplication
public class MicroserviceLinehualAPI{



    public static void main(String[] args) {
        SpringApplication.run(MicroserviceLinehualAPI.class, args);
    }


    /**
     * 常规返回，交由Spring进行管理
     * @return
     */
    @Bean
    public BasicResult basicResult(){
        return new BasicResult();
    }


    /**
     * 注册到spring容器中
     * @return
     */
    /*@Bean
    public FilterRegistrationBean
    testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpFilter");
        registration.setOrder(1);
        return registration;
    }*/


}
