package com.mycompany.app.myapp;

import java.util.List;

public class CommonUtils {
    private CommonUtils() {
    }

    public static void printLines(List<String> lines){
        for(String line: lines){
            System.out.println(line);
        }
    }
}
