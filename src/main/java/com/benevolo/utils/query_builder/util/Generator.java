package com.benevolo.utils.query_builder.util;

import java.util.Random;

public class Generator {

    public static String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < 10; i++) {
            stringBuilder.append(alphabet.charAt(new Random().nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

}
