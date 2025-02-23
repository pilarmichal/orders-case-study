package com.pilarmichal.orderscasestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrdersCaseStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersCaseStudyApplication.class, args);
    }

}
