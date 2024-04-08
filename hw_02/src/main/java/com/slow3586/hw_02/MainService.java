package com.slow3586.hw_02;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainService {
    @GetMapping(path = "health")
    public void health(){}
}
