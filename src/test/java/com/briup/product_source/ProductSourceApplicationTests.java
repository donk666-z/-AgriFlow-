package com.briup.product_source;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.UUID;

@SpringBootTest
class ProductSourceApplicationTests {

    @Test
    void test_StringUtils() {
        String str1 = null;
        String str2 = "";
        String str3 = "  ";
        String str4 = " hello ";
        System.out.println(StringUtils.hasText(str1));  //false
        System.out.println(StringUtils.hasText(str2));  //false
        System.out.println(StringUtils.hasText(str3));  //false
        System.out.println(StringUtils.hasText(str4));  //true
    }

    //自动生成随机序列 UUID
    @Test
    void test_uuid() {
        //获取随机字符串 - 连接，
        // 如：93fb4166-b453-46d1-b865-b91c8b2b0a3d
        UUID uuid = UUID.randomUUID();
        System.out.println("uuid: " + uuid);
        //去除 -
        // 如：93fb4166b45346d1b865b91c8b2b0a3d
        String str = uuid.toString().replace("-", "");
        System.out.println("str: " + str);

        //简化写法
        String id = UUID.randomUUID().toString().replace("-","");
        System.out.println(id);
    }

}
