package com.ly.review.base._05;

public class Main {
    public static int f(int n){
        if(n==1 || n==2){
            return n;
        }
        return f(n-2)+f(n-1);
    }

    public static void main(String[] args) {
        System.out.println(f(1)); //1
        System.out.println(f(2)); //2
        System.out.println(f(3)); //3
        System.out.println(f(4)); //5
        System.out.println(f(5)); //8
    }
}
