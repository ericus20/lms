package com.developersboard.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Eric on 5/24/2018.
 *
 * @author Eric Opoku
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.developersboard.lms.proxy"})
@SpringBootApplication
public class LMS {

    public static void main(String[] args) {
        SpringApplication.run(LMS.class, args);
    }

}
