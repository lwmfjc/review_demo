package com.ly.review.base;

import java.util.HashSet;
import java.util.Objects;

class A{
    private String name;
    private int age;

    public <E> int  geA(E e){
        System.out.println(e.toString());
        return 1;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return age == a.age ||
                Objects.equals(name, a.name);
    }

    @Override
    public int hashCode() {
        return 3;//Objects.hash(name, age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
public class LyTest01 {

    public static void main(String[] args) {
        /*A a1=new A();
        a1.setAge(10);
        a1.setName("a1");
        A a2=new A();
        a2.setAge(20);
        a2.setName("a2");
        HashSet<A> hashSet=new HashSet<>();
        hashSet.add(a1);
        hashSet.add(a2);
        boolean contains = hashSet.contains(a2);
        System.out.println (contains);*/
       /* String s1="abc";
        String s2=new String("abc");
        String s3="abc";
        String s4=new String("abc");
        System.out.println(s1==s2);
        System.out.println(s1==s3);
        A a
                =new A();
        a.<Object>geA(new Object());*/
        float a=2.0f-1.9f;
        float b=3.1f- 3f;
        System.out.println(a);
        System.out.println(b);
    }
}
