package com.dongguo.exceldemo.easyexcel.service.impl;

import com.dongguo.exceldemo.easyexcel.service.TestService;
import com.dongguo.exceldemo.util.ThreadLocalUtil;
import org.springframework.stereotype.Service;

/**
 * @author dongguo
 * @date 2023/2/8
 * @description:
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public void getBatchId() {
        Object batchId = ThreadLocalUtil.get("batchId");
        System.out.println(batchId);
    }
}
