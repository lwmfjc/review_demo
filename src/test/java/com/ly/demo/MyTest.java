package com.ly.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
public class MyTest {

    @Test
    public void myTest() throws IOException, InterruptedException {
        // 创建 WatchService 对象
        WatchService watchService = FileSystems.getDefault().newWatchService();

// 初始化一个被监控文件夹的 Path 类:
        Path path = Paths.get("F:\\java_test\\git\\hexo\\review_demo\\src\\com\\hp");
// 将这个 path 对象注册到 WatchService（监控服务） 中去
        WatchKey key = path.register(
                watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE
                ,StandardWatchEventKinds.ENTRY_MODIFY);

        while ((key = watchService.take()) != null) {
            System.out.println("检测到了事件--start--");
            for (WatchEvent<?> event : key.pollEvents()) {
                // 可以调用 WatchEvent 对象的方法做一些事情比如输出事件的具体上下文信息
                System.out.println("event.kind().name()"+event.kind().name());
            }
            key.reset();
            System.out.println("检测到了事件--end--");
        }

    }

    @Test
    public void bb(){
        BufferedInputStream bufferedInputStream;
        InputStream reader;
        LinkedList linkedList;
        String a="11,22,33,21";
        String b="33,21,11,22";
        //分割字符串
        String[] split = a.split(",");
        String[] split1 = b.split(",");
        List<String> list=new ArrayList<>();
        ArrayList arrayList;
        Arrays.asList();
        //ConcurrentHashMap
        //hashMap
        HashMap<String,Integer> map1=new HashMap<>();
        HashMap<String,Integer> map2=new HashMap<>();

        for(String s:split){
            map1.put(s,map1.get(s)==null ? 1 : map1.get(s)+1);
        }
        System.out.println(map1);

        for(String s:split1){
            map2.put(s,map2.get(s)==null ? 1 : map2.get(s)+1);
        }
        System.out.println(map2);
        //遍历map1
        Set<Map.Entry<String, Integer>> entries = map1.entrySet();
        for(Map.Entry<String,Integer> entry :entries){
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(!value.equals(map2.get(key))){
                System.out.println("不相等");
                break;
            }
        }

    }

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
    public void a() {
        log.info("hello,world!");
        Unsafe unsafe = reflectGetUnsafe();
        int size = 4;
        long addr = unsafe.allocateMemory(size);
        long addr3 = unsafe.reallocateMemory(addr, size * 2);
        System.out.println("addr: " + addr);
        System.out.println("addr3: " + addr3);
        try {
            unsafe.setMemory(null, addr, size, (byte) 1);
            for (int i = 0; i < 2; i++) {
                unsafe.copyMemory(null, addr, null, addr3 + size * i, 4);
            }
            System.out.println(unsafe.getInt(addr));
            System.out.println(unsafe.getLong(addr3));
        } finally {
            unsafe.freeMemory(addr);
            unsafe.freeMemory(addr3);
        }
    }

    @Test
    public void mainIn() {
        File directory = new File("F:\\java_test\\git\\hexo\\review_demo\\src\\test\\resources\\a");
        b(directory);
    }

    @Test
    public void b(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            List<File> filesList = Arrays.asList(files);

            //找到所有的非文件夹
            List<File> notDirectoryS = filesList.stream().filter(File::isFile).collect(Collectors.toList());
            //用来比较的数组列表
            List<File> toBeCompare = new ArrayList<>(notDirectoryS);

            Iterator<File> iterator = notDirectoryS.iterator();
            //处理过的文件名称
            Set<String> namesHandle=new HashSet<>();
            while (iterator.hasNext()) {
                File file = iterator.next();
                String name = file.getName();
                //System.out.println(file.getPath());
                if(namesHandle.contains(name)){
                    continue;
                }

                //是否是自定义生成的后缀
                if (isAutoExtra(name)) {

                    //进行比较
                    Iterator<File> iteratorCompare = toBeCompare.iterator();
                    List<File> fileRepeat=new ArrayList<>();
                    fileRepeat.add(file);
                    namesHandle.add(file.getName());
                    while (iteratorCompare.hasNext()){
                        File fileCompare = iteratorCompare.next();
                        //名字相同直接不处理
                        String nameCompare = fileCompare.getName();
                        if (nameCompare.equals(name)) {
                            continue;
                        }
                        String nameCompareRemove = removeExtra(name);
                        if(StringUtils.isNotBlank(nameCompareRemove)){
                            String s1 = removeExtra(nameCompare);
                            //要比较的文件名去掉前缀之后
                            if(nameCompareRemove.equals(nameCompare) || nameCompareRemove.equals(s1)){
                                fileRepeat.add(fileCompare);
                                namesHandle.add(fileCompare.getName());
                                iteratorCompare.remove();

                            }
                        }
                    }


                     if (fileRepeat.size() > 1) {
                        log.info("--start--存在重复的文件\n");
                         fileRepeat.forEach(file1 -> {
                            log.info(file1.getName());
                        });
                         File fileMax = fileRepeat.stream().max((o1, o2) -> {
                             String name1 = o1.getName();
                             String name2 = o2.getName();

                             Long name1Num = getExtraNum(name1);
                             Long name2Num = getExtraNum(name2);
                             return name1Num.compareTo(name2Num);
                         }).get();
                         log.info("文件最大的是：" + fileMax.getName());
                         //删除其他的文件
                         Iterator<File> iteratorDelete = fileRepeat.iterator();
                         while (iteratorDelete.hasNext()){
                             File next = iteratorDelete.next();
                             if(next.getName().equals(fileMax.getName())){
                                 continue;
                             }
                             if(!next.exists()){
                                 log.info("文件不存在");
                             }else {
                                 next.delete();
                                 iteratorDelete.remove();
                             }
                         }
                         //重命名最大文件
                         String fileMaxName = fileMax.getName();
                         log.info("去除自定义生成的数字{}",removeExtra(fileMaxName));
                         String absolutePath = fileMax.getAbsolutePath();//文件路径
                         //文件路径去除文件名
                         String substringWithoutFileName = absolutePath.substring(0, absolutePath.length() - fileMaxName.length());
                         log.info(substringWithoutFileName);
                         log.info("重命名:{}",substringWithoutFileName+removeExtra(fileMaxName));
                         fileMax.renameTo(new File(substringWithoutFileName+removeExtra(fileMaxName)));
                         log.info("--end--存在重复的文件\n");
                    }
                }
            }

            List<File> directoryS = filesList.stream().filter(File::isDirectory).collect(Collectors.toList());
            for (File file:directoryS) {
                b(file);
            }

        }
    }

    private Long getExtraNum(String fileName) {
        if(isAutoExtra(fileName)){

            fileName= fileName.replaceAll("_", "");
            int i = fileName.lastIndexOf(".");
            if (i > 0) {
                fileName = fileName.substring(0, i);
            }

            fileName=fileName.substring(fileName.length()-12);
            return Long.parseLong(fileName);
        }
        return 0L;
    }
    private String removeExtra(String fileName) {

        String fileNameSource=fileName;
        int i = fileName.lastIndexOf(".");
        String fixStr = fileName.substring(i);//后缀名
        if (i > 0) {
            fileName = fileName.substring(0, i);
        }

        //判断是否是自定义拓展的
        if(isAutoExtra(fileNameSource)) {
            if (i > 0) {
                fileName = fileName.substring(0, i);
                int length = fileName.length();
                if (length > 14) {
                    String s = fileName.substring(0, length - 14);
                    return s+fixStr;
                }
            }
        }
        return fileName+fixStr;
    }

    private boolean isAutoExtra(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
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
