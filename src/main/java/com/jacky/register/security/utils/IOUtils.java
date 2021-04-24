package com.jacky.register.security.utils;

import java.io.BufferedReader;
import java.util.stream.Collectors;

public class IOUtils {
    public static String  toString(BufferedReader reader){
        StringBuilder builder=new StringBuilder();
        var lines=reader.lines();
        for(String s:lines.collect(Collectors.toList())){
            builder.append(s);
        }

        return builder.toString();
    }
}
