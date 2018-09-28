package com.semcon.oil.carpoc;

import java.util.List;
import java.util.Random;

public class Util {

    public static void populateHex(List<String> colors) {
        colors.add("0");
        colors.add("1");
        colors.add("2");
        colors.add("3");
        colors.add("4");
        colors.add("5");
        colors.add("6");
        colors.add("7");
        colors.add("8");
        colors.add("9");
        colors.add("A");
        colors.add("B");
        colors.add("C");
        colors.add("D");
        colors.add("E");
        colors.add("F");
    }


    public static String getRandomColor(List<String> colors) {

        StringBuilder s = new StringBuilder("#");
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            s.append(colors.get(rand.nextInt(colors.size())));
        }
        return s.toString();
    }
}
