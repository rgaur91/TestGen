package org.testgen.util;

public class Assertion {
    public static void assertTrue(boolean exp){
        assertTrue(exp, "Assertion Failed, invalid input!");
    }

    public static void assertTrue(boolean exp, String msg){
        if (!exp){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void assertNotBlank(String s, String msg){
        assertTrue(s!=null && !s.isBlank(), msg);
    }
}
