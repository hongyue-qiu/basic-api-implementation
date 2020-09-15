package com.thoughtworks.rslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RsListApplication {


    public static void main(String[] args) {
        SpringApplication.run(RsListApplication.class, args);
    }

}
