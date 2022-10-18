package com.ly.review.base;

enum X{
    A,B,C,D
}

public class SwitchTest {
    public static void main(String[] args) {
        String[] args1 = args;

        int a = Integer.parseInt(args1[0]);
        switch (a) {
            case 3:
                System.out.println("1");
                break;
            case 4:
                System.out.println("1");
                break;
            default:
                System.out.println("2");
        }


        X b = X.B;
        switch (b) {
            case  A:
                System.out.println("1");
                break;
            case B:
                System.out.println("1");
                break;
            default:
                System.out.println("2");
        }
    }
}
