package com.pracex.programming.dmaker.controller;


import com.pracex.programming.dmaker.dto.*;
import com.pracex.programming.dmaker.exception.DMakerException;
import com.pracex.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


// 빈 타입의 지정 컨트롤러 + 리스폰스바디(json으로 값을 보내줄 것이다.)
@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;


    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return dMakerService.getAllEmployedDevelopers();
    }
    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ) {
        // GET /developers HTTP/1.1
        log.info("GET /developer/{memberId} HTTP/1.1");
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDevelopers(
           @Valid // 벨리데이션 적용해줌
           @RequestBody CreateDeveloper.Request request
            ) {
        // request 정보 확인
        log.info("request {} ",request);
        // 요청 실행, response 전달까지
        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDevelopper(
            @PathVariable String memberId,
            @RequestBody EditDeveloper.Request request
    ) {
        // GET /developers HTTP/1.1
        log.info("Put /developer/{memberId} HTTP/1.1");
        return dMakerService.editDevelopper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable String memberId
    ) {

        return dMakerService.deleteDeveloper(memberId);
    }

//    @ResponseStatus(value = HttpStatus.CONFLICT)
    // 상태코드 변경
//    @ExceptionHandler(DMakerException.class)
//    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request){
//        // 해당 컨트롤러의 Global Error 처리 1
//        log.error("errorCode : {} , url : {} , message : {}",
//                e.getDMakerErrorCode(),
//                request.getRequestURI(),
//                e.getDetailMessage());
//        return DMakerErrorResponse.builder()
//                .errorCode(e.getDMakerErrorCode())
//                .errorMessage(e.getDetailMessage())
//                .build();
//    }

}
