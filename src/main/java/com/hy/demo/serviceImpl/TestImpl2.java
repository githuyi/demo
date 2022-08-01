package com.hy.demo.serviceImpl;

import com.hy.demo.service.Test;
import org.springframework.stereotype.Service;

/**
 * @author huyi
 * @date 2022/6/1 上午 10:13
 * @description
 */
//@Service("TestImpl2")
public class TestImpl2 implements Test {
    @Override
    public void test() {
        System.out.println("====================== TestImpl2");
    }
}
