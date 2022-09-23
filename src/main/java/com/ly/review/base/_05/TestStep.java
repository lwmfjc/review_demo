package com.ly.review.base._05;

public class TestStep {

    public static void main(String[] args) {
        System.out.println(loop(4));
    }

    public static int f(int n) {
        if (n < 1) {
            throw new IllegalArgumentException(n + "不能小于1");
        }
        if (n == 1 || n == 2) {
            return n;
        }
        return f(n - 2) + f(n - 1);
    }

    public static int loop(int n){

        if (n < 1) {
            throw new IllegalArgumentException(n + "不能小于1");
        }
        if (n == 1 || n == 2) {
            return n;
        }
        int one=2;//最后只走1步，会有2种走法
        int two=1;//最后走2步，会有1种走法
        int sum=0;
        for(int i=3;i<=n;i++){
            //最后跨两级台阶+最后跨一级台阶的走法
            sum=two+one;
            two=one;
            one=sum;
        }
        return sum;
    }
}
