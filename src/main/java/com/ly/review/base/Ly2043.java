package com.ly.review.base;

public class Ly2043 {
    public static void main(String[] args) {
        //先查找常量池中是否有"abc"，如果有直接返回在常量池中的引用,
        //如果没有，则在常量池中创建"abc",然后返回该引用
        String a="abc";

        //先查找常量池中是否有"abc"，如果有则在堆内存中创建对象，然后返回堆内存中的地址
        //如果没有，则先在常量池中创建字符串对象，然后再在堆内存中创建对象，最后返回堆内存中的地址
        String ab=new String("abc");
        System.out.println(a==ab);//true

        //intern() //判断常量池中是否有ab对象的字符串，如果存在"abc"则返回"abc"在
        //常量池中的引用，如果不存在则在常量池中创建,
        //并返回"abc"在常量池中的引用
        System.out.println(a==ab.intern());//true
    }
}
