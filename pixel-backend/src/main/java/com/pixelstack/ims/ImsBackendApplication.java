package com.pixelstack.ims;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pixelstack.ims.mapper")
public class ImsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsBackendApplication.class, args);
    }

}