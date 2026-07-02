package com.infiproton.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AdvancedRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedRagApplication.class, args);
    }

}
