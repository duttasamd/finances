package com.samratdutta.finances.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private Environment env;
    @GetMapping("/")
    public String test() {
        return "Finances API server running... Datasource URL : " + env.getProperty("spring.datasource.url");
    }
}
