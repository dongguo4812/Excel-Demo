package com.dongguo.exceldemo.easyexcel.controller;

import cn.hutool.core.util.IdUtil;
import com.dongguo.exceldemo.easyexcel.service.TestService;
import com.dongguo.exceldemo.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TestService testService;
    @PostMapping("/test")
    public void test() {
        long batchId = IdUtil.getSnowflake().nextId();
        ThreadLocalUtil.put("batchId",batchId);
        testService.getBatchId();
        log.info("test success");
    }
}
