package com.yinchd.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class WebBootApplicationTests {

    @Test
    void test1() {
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("sex");
        list.add("createTime");
        list.add("an");
        Collections.sort(list);
        System.out.println(list);
    }

}
