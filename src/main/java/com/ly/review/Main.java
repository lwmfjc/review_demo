package com.ly.review;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    public static void main(String[] args)  {
        String classPath = System.getProperty("user.dir");
        log.info("当前目录:{}",classPath);

        /*File directory = new File("F:\\java_test\\git\\hexo\\review_demo\\src\\test\\resources\\a");
        new Main().b(directory);*/
    }

    public   void b(File directory) {
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
                        if(!"".equals(nameCompareRemove)){
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
