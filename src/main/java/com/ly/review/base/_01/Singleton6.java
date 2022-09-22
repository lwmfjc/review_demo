package com.ly.review.base._01;

public class Singleton6 {
    private Singleton6(){

    }
    private static class Inner{
        private static final Singleton6 INSTANCE=new Singleton6();
    }
    public static Singleton6 getInstance(){
        return Inner.INSTANCE;
    }
}
