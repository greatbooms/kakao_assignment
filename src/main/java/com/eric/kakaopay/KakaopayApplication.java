package com.eric.kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class KakaopayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaopayApplication.class, args);
    }

}
