package com.nju.nnt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.nju.nnt.mapper")
public class NntApplication {

    public static void main(String[] args) {
          SpringApplication.run(NntApplication.class, args);
    }

}
