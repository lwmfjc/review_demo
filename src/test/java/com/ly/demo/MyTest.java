package com.ly.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MyTest {

    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {

            return null;
        }
    }
    @Test
    public void a(){
        log.info("hello,world!");
        Unsafe unsafe = reflectGetUnsafe();
        int size = 4;
        long addr = unsafe.allocateMemory(size);
        long addr3 = unsafe.reallocateMemory(addr, size * 2);
        System.out.println("addr: "+addr);
        System.out.println("addr3: "+addr3);
        try {
            unsafe.setMemory(null,addr ,size,(byte)1);
            for (int i = 0; i < 2; i++) {
                unsafe.copyMemory(null,addr,null,addr3+size*i,4);
            }
            System.out.println(unsafe.getInt(addr));
            System.out.println(unsafe.getLong(addr3));
        }finally {
            unsafe.freeMemory(addr);
            unsafe.freeMemory(addr3);
        }
    }

    @Test
    public void mainIn(){
        File directory = new File("F:\\java_test\\git\\hexo\\review_demo\\src\\test\\resources\\a");
        b(directory);
    }

    @Test
    public void b(File directory){
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            /*for(int n=0;n<files.length;n++){
                System.out.println(files[n].getName());
            }*/
            List<File> filesList = Arrays.asList(files);
            //找到所有的非文件夹
            List<File> notDirectoryS = filesList.stream().filter(File::isFile).collect(Collectors.toList());
            notDirectoryS.forEach(file -> {
                String name2 = file.getName();
                //是否是自定义生成的后缀
                if(isAutoExtra(name2)){
                    log.info(name2);
                    List<File> filesRepeat=new ArrayList<>();
                    filesRepeat.add(file);

                    List<File> collect = notDirectoryS.stream().filter(file1 -> {
                        //名字相同直接不处理
                        if(file1.getName().equals(name2)){
                            return false;
                        }


                        return false;
                    })
                            .collect(Collectors.toList());
                    if(collect.size()>1){
                        log.info("--start--存在重复的文件\n");
                        collect.forEach(file1 -> {
                            log.info(file1.getName());
                        });
                        log.info("--end--存在重复的文件\n");
                    }
                }
            });

            List<File> directoryS = filesList.stream().filter(File::isDirectory).collect(Collectors.toList());
            for (File file:directoryS) {
                b(file);
            }

        }
    }


    private boolean isAutoExtra(String fileName){
        int i = fileName.lastIndexOf(".");
        if(i > 0) {
            fileName = fileName.substring(0, i);
            int length = fileName.length();
            if (length > 14) {
                String s = fileName.replaceAll("_", "");
                int lengthReplace = s.length();
                if (lengthReplace > 12) {
                    String substring = s.substring(lengthReplace - 12);
                    if (substring.matches("^[0-9]*$")) {
                        return true;
                    }
                }
                //System.out.println("去除后的文件名为："+substring);
            }
        }
        return false;
    }

}
