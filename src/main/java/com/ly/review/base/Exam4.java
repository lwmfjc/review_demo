package com.ly.review.base;

import java.util.Arrays;

public class Exam4 {
    public static void main(String[] args) {
        //当使用new的时候，一定在堆中新开辟的空间
        Integer a1= new Integer(12);
        Integer b1= new Integer(12);
        System.out.println(a1 == b1);//false
        Integer a2= -128;
        Integer b2= -128;
        System.out.println(a2 == b2);//true
        Integer a21= -129;
        Integer b21= -129;
        System.out.println(a21 == b21);//false
        Integer a3=  127;
        Integer b3=  127;
        System.out.println(a3 == b3);//true
        Integer a4=  22;
        Integer b4=  22;
        System.out.println(a4 == b4);//true
        Integer a31=  128;
        Integer b31=  128;
        System.out.println(a31 == b31);//false

        int i = 1;
        String str = "hello";
        Integer num = 2;
        int[] arr = {1, 2, 3, 4, 5};
        MyData my = new MyData();

        change(i, str, num, arr, my);
        System.out.println("i = " + i);
        System.out.println("str = " + str);
        System.out.println("num = " + num);
        System.out.println("arr = " + Arrays.toString(arr));
        System.out.println("my.a = " + my.a);
        //1 hello 2 22345 11
    }

    public static void change(int j, String s, Integer n, int[] a,
                              MyData m) {
        j+=1;
        s+="world";
        n+=1;
        a[0]+=1;
        m.a+=1;
    }

}

class MyData {
    int a = 10;
}
