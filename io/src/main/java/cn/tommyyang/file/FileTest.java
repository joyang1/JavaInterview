package cn.tommyyang.file;

import java.util.stream.IntStream;

/**
 * @Author : TommyYang
 * @Time : 2019-09-09 23:30
 * @Software: IntelliJ IDEA
 * @File : FileTest.java
 */
public class FileTest {

    public static void main(String[] args) {
        new Thread(()-> {
            long startTime = System.currentTimeMillis();
            BigFileWriter fileWriter = new BigFileWriter("/Users/tommy/Documents/aa.txt");
            IntStream.range(0, 500000).parallel().forEach(index -> fileWriter.writeFile("user:" + index + "\n"));
            fileWriter.close();
            long endTime = System.currentTimeMillis();
            System.out.println("big file writer const:" + (endTime - startTime) + " ms");
        }).start();

        new Thread(()-> {
            long startTime = System.currentTimeMillis();
            BigFileWriter1 fileWriter = new BigFileWriter1("/Users/tommy/Documents/bb.txt");
            IntStream.range(0, 500000).parallel().forEach(index -> fileWriter.writeFile("user:" + index + "\n"));
            fileWriter.close();
            long endTime = System.currentTimeMillis();
            System.out.println("big file writer 1 const:" + (endTime - startTime) + " ms");
        }).start();


        new Thread(()-> {
            long startTime = System.currentTimeMillis();
            IntStream.range(0, 500000).parallel().forEach(index -> FileUtils.writeFile("/Users/tommy/Documents/cc.txt", "user:" + index + "\n"));
            long endTime = System.currentTimeMillis();
            System.out.println("file utils const:" + (endTime - startTime) + " ms");
        }).start();
    }

}
