package com.hy.demo.controller;

import com.hy.demo.service.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * @author huyi
 * @date 2022/6/1 上午 10:14
 * @description
 */
@Controller
public class TestController {

    @Autowired
    @Qualifier("TestImpl2")
    Test test;

    @org.junit.Test
    public void test(){
        test.test();
    }

}
