package com.pracex.programming.dmaker.controller;


import com.pracex.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


// 빈 타입의 지정 컨트롤러 + 리스폰스바디(json으로 값을 보내줄 것이다.)
@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;


    @GetMapping("/developers")
    public List<String> getAllDevelopers(){
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow","elsa","olf");
    }
    // 잘못된 리소스
    @GetMapping("/create-developers")
    public List<String> createDevelopers(){
        // GET /developers HTTP/1.1
        log.info("GET /create-developers HTTP/1.1");
        dMakerService.createDeveloper();
        return Collections.singletonList("Olaf");
    }

}
