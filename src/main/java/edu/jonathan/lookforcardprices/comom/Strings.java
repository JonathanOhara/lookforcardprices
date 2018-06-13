package edu.jonathan.lookforcardprices.comom;

import java.util.List;

public final class Strings {
    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    public static long countCharOccurrencesIn(String string, char character){
        return string.chars().filter(ch -> ch == character).count();
    }

    public static String implodeListIntoString(List<String> list, char delimiter) {
        StringBuilder builder = new StringBuilder();
        list.stream().forEach( data -> builder.append(data).append(delimiter) );
        return builder.toString();
    }
}
