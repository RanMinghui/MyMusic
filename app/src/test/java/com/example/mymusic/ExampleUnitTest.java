package com.example.mymusic;

import org.junit.Test;

import static org.junit.Assert.*;

import java.security.PublicKey;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public enum ENUM {
        ENUM_TEST_0001,
        ENUM_TEST_0002,
        ENUM_TEST_0003,
        ENUM_TEST_0004
    }

    @Test
    public void enumTest(){
        ENUM enum0001 = ENUM.ENUM_TEST_0001;
        //enum0001:ENUM_TEST_0001   enum0001:ENUM_TEST_0001
        System.out.println("enum0001:"+ENUM.ENUM_TEST_0001.name());
    }

    @Test
    public void Test0001(){
        final String s1 = "test001";
        String s2 = "test002";
        String s3 = "test002";

        System.out.println(s2 == s3);
    }
}