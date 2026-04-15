package com.sitool.servicedesk.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestAuthController {

    @GetMapping
    public String securedEndpoint() {
        return "You are authenticated!";
    }

}
