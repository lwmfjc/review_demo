package com.ly.review;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class Main {
    Pattern ptCompileAll = Pattern.compile("_[0-9]{6}_[0-9]{6}(\\([0-9]+\\))*\\..+");
    Pattern ptCompileNum = Pattern.compile("_[0-9]{6}_[0-9]{6}");

    public static void main(String[] args) {
        String classPath = System.getProperty("user.dir");
        log.info("当前目录:{}", classPath);
        // classPath = "D:\\Users\\ly\\Documents\\git\\hexo\\review_demo\\src\\test\\resources\\a";

        File directory = new File(classPath);
        new Main().b(directory);
    }

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
            Set<String> namesHandle = new HashSet<>();
            while (iterator.hasNext()) {
                File file = iterator.next();
                String name = file.getName();
                //System.out.println(file.getPath());
                if (namesHandle.contains(name)) {
                    continue;
                }

                //是否是自定义生成的后缀
                if (isAutoExtra(name)) {

                    //进行比较
                    Iterator<File> iteratorCompare = toBeCompare.iterator();
                    List<File> fileRepeat = new ArrayList<>();
                    fileRepeat.add(file);
                    namesHandle.add(file.getName());
                    while (iteratorCompare.hasNext()) {
                        File fileCompare = iteratorCompare.next();
                        //名字相同直接不处理
                        String nameCompare = fileCompare.getName();
                        if (nameCompare.equals(name)) {
                            continue;
                        }
                        String nameCompareRemove = removeExtra(name);
                        if (!"".equals(nameCompareRemove)) {
                            String s1 = removeExtra(nameCompare);
                            //要比较的文件名去掉后缀之后
                            if (nameCompareRemove.equals(nameCompare) || nameCompareRemove.equals(s1)) {
                                fileRepeat.add(fileCompare);
                                namesHandle.add(fileCompare.getName());
                                iteratorCompare.remove();

                            }
                        }
                    }


                    if (fileRepeat.size() > 1) {
                        log.info("--start--重复的文件\n");
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
                        log.info("--end--重复的文件\n");
                        log.info("文件最大的是：" + fileMax.getName());

                        //拷贝文件名最大的文件

                        //最大文件去除自定义生成的后缀
                        String fileMaxName = fileMax.getName();
                        log.info("需要拷贝的文件{}", fileMaxName);
                        String absolutePath = fileMax.getAbsolutePath();//文件路径
                        //文件路径去除文件名
                        String substringWithoutFileName = absolutePath.substring(0, absolutePath.length() - fileMaxName.length());
                        log.info("从：{}", fileMax.getAbsolutePath());
                        log.info("拷贝到:{}", substringWithoutFileName + removeExtra(fileMaxName));
                        try {
                            File fileTo = new File(substringWithoutFileName + removeExtra(fileMaxName));
                            FileInputStream fileInputStream = new FileInputStream(fileMax);
                            FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
                            IOUtils.copy(fileInputStream, fileOutputStream);
                            fileInputStream.close();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }


                        //删除其他的文件
                        Iterator<File> iteratorDelete = fileRepeat.iterator();
                        while (iteratorDelete.hasNext()) {
                            File next = iteratorDelete.next();
                            if (next.getName().equals(removeExtra(fileMaxName))) {
                                continue;
                            }
                            if (!next.exists()) {
                                log.info("文件不存在");
                            } else {
                                boolean delete = next.delete();
                                log.info("文件：{}--删除{}", next.getName(), delete ? "成功" : "失败");
                                iteratorDelete.remove();
                            }
                        }

                    }
                }
            }

            List<File> directoryS = filesList.stream().filter(File::isDirectory).collect(Collectors.toList());
            for (File file : directoryS) {
                b(file);
            }

        }
    }

    /**
     * 获取自定义中的数字
     *
     * @param fileName
     * @return
     */
    private Long getExtraNum(String fileName) {
        Long num = 0L;
        //如果是自定义拓展
        if (isAutoExtra(fileName)) {
            Matcher matcher = ptCompileNum.matcher(fileName);
            String s = "";
            while (matcher.find()) {
                s = matcher.group();
            }
            if (!"".equals(s)) {
                String s1 = s.replaceAll("_", "");
                num = Long.parseLong(s1);
            }
        }
        return num;
    }

    /**
     * 去除自定义格式
     *
     * @param fileName
     * @return
     */
    private String removeExtra(String fileName) {
        //如果是自定义拓展
        if (isAutoExtra(fileName)) {

            int i = fileName.lastIndexOf(".");
            String fixStr = fileName.substring(i);//后缀名

            fileName = fileName.replaceAll(ptCompileAll.pattern(), "");
            fileName += fixStr;
        }
        return fileName;
    }

    /**
     * 是否是加了自定义格式
     *
     * @param fileName
     * @return
     */
    private boolean isAutoExtra(String fileName) {
        Matcher matcher = ptCompileAll.matcher(fileName);
        return matcher.find();
    }

}
