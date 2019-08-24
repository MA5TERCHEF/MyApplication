package com.example.lib;

import java.util.Stack;

public class MyClass {
    public static void main(String[] args) {

        int flag = 0;
        Stack<Integer> s = new Stack<>();
        for (int i = 1; i <= 16; i++) {
            if (flag == 0) {
                System.out.print(i);
                if (i % 4 == 0) {
                    System.out.println();
                    flag = 1;
                }
            } else {
                s.push(i);
            }
            if (s.size() == 4) {
                for (int j = 0; j < 4; j++) {

                    System.out.print(s.pop());
                }
                System.out.println();
                flag = 0;
            }
        }
    }
}
