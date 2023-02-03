package com.dongguo.exceldemo.easyexcel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@RestController
@Slf4j
public class TestController {

    @PostMapping("/test")
    public void test() {
        log.info("test success");
    }
}
