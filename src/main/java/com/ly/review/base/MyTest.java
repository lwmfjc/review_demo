package com.ly.review.base;

public class MyTest {
    public MyTest(){

    }
    public static void a(){
        new Integer(1);
    }

    public static void main(String[] args) {
        Boolean t=new Boolean(true);
        Boolean f=new Boolean(true);
        System.out.println(t==f); //false
        System.out.println(t.equals(f)); //true

        Boolean t1=Boolean.valueOf(true);
        Boolean f1=Boolean.valueOf(true);
        System.out.println(t1==f1); //true

        System.out.println(Boolean.TRUE==Boolean.TRUE); //true

    }
}
  class MyTestChild extends MyTest{

      public static void a(){}
}