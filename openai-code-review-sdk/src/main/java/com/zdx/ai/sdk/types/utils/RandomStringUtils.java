package com.zdx.ai.sdk.types.utils;

import java.util.Random;

/**
 * @author zdx
 * @description
 * @create 2026-03-26 11:36
 */
public class RandomStringUtils {
    public static String randomString(int length) {
        String charachers =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            sb.append(charachers.charAt(random.nextInt(charachers.length())));
        }
        return sb.toString();
    }
}
