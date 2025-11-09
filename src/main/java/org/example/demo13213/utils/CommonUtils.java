package org.example.demo13213.utils;



import org.example.demo13213.exception.BaseException;

import java.util.regex.Pattern;

public class CommonUtils {
    @FunctionalInterface
    public interface Checker {
        boolean check(); //true false
    }

    public static void throwIf(Checker checker, BaseException ex) {
        if (checker.check()) {
            throw ex;
        }
    }

    //ternary operatorunun daha tekmil formasi
    //true gelse exception atilir
    //false gelse davam edirik

}
