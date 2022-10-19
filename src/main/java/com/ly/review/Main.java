package com.ly.review;

import lombok.extern.slf4j.Slf4j;
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
    Pattern ptCompileAll = Pattern.compile("_[0-9]{6}_[0-9]{6}([ ]\\([0-9]+\\))*\\..+");
    Pattern ptCompileCompare = Pattern.compile("_[0-9]{6}_[0-9]{6}([ ]\\([0-9]+\\))*");
    private final String extraKeyDate = "extraDate";
    private final String extraKeyNo = "extraNo";

    public static void main(String[] args) {
        String classPath = System.getProperty("user.dir");
        log.info("当前目录:{}", classPath);
        //classPath = "F:\\java_test\\git\\hexo\\review_demo\\src\\test\\resources\\a";

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

                            Map<String, Long> extraNum1 = getExtraNum(name1);
                            Long extraDate1 = extraNum1.get(extraKeyDate);
                            Long extraNo1 = extraNum1.get(extraKeyNo);


                            Map<String, Long> extraNum2 = getExtraNum(name2);
                            Long extraDate2 = extraNum2.get(extraKeyDate);
                            Long extraNo2 = extraNum2.get(extraKeyNo);
                            int dateCompare = extraDate1.compareTo(extraDate2);
                            if (dateCompare == 0) {
                                return extraNo1.compareTo(extraNo2);
                            } else {
                                return dateCompare;
                            }
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

                    } else {
                        File fileFrom = fileRepeat.get(0);
                        String name1 = fileFrom.getName();
                        if (isAutoExtra(name1)) {
                            //改成非自定义拓展文件名
                            String s = removeExtra(name1);
                            log.info("--start修改自定义拓展文件名");
                            log.info("原文件名:{}", fileFrom.getAbsolutePath());
                            try {
                                //copy并删除
                                String absolutePath = fileFrom.getAbsolutePath();
                                String substringWithoutFileName = absolutePath.substring(0, absolutePath.length()
                                        - name1.length());

                                File fileTo = new File(substringWithoutFileName + s);
                                FileInputStream fileInputStream = new FileInputStream(fileFrom);
                                FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
                                IOUtils.copy(fileInputStream, fileOutputStream);
                                fileInputStream.close();
                                fileOutputStream.close();
                                //删除源文件
                                boolean delete = fileFrom.delete();
                                log.info("文件：{}--删除{}", fileFrom.getName(), delete ? "成功" : "失败");

                                log.info("新文件名:{}", fileTo.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            log.info("--end修改自定义拓展文件名");
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
    private Map<String, Long> getExtraNum(String fileName) {
        Map<String, Long> hashMap = new HashMap<>();
        hashMap.put(this.extraKeyDate, 0L);
        hashMap.put(this.extraKeyNo, 0L);

        //如果是自定义拓展
        if (isAutoExtra(fileName)) {
            Matcher matcher = ptCompileCompare.matcher(fileName);
            String s = "";
            while (matcher.find()) {
                s = matcher.group();
            }


            Matcher matcherDate = Pattern.compile("_[0-9]{6}_[0-9]{6}")
                    .matcher(s);
            Matcher matcherNo = Pattern.compile("(\\([0-9]+\\))")
                    .matcher(s);
            String sDate = "";
            String sNo = "";

            //处理时间
            while (matcherDate.find()) {
                sDate = matcherDate.group();
            }
            if (!"".equals(sDate)) {
                hashMap.put(this.extraKeyDate, Long.parseLong(sDate.replace("_", "")));
            }

            //处理序号
            while (matcherNo.find()) {
                sNo = matcherNo.group();
            }
            if (!"".equals(sNo)) {
                hashMap.put(this.extraKeyNo, Long.parseLong(sNo.replace("(", "")
                        .replace(")", "")));
            }
        }
        return hashMap;
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
