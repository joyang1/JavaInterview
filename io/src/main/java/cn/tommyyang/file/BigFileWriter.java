package cn.tommyyang.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @Author : TommyYang
 * @Time : 2019-09-09 16:36
 * @Software: IntelliJ IDEA
 * @File : BigFileWriter.java
 */
public class BigFileWriter {

    private FileOutputStream outStream = null;
    private BufferedWriter bfWriter = null;

    private String filePath;

    public BigFileWriter(String filePath) {
        this.filePath = filePath;
        open();
    }

    private void open() {
        try {
            outStream = new FileOutputStream(this.filePath, true);
            bfWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
        } catch (Exception e) {
            System.out.println("get big file writer error");
            e.printStackTrace();
        }
    }

    public void writeFile(String content) {
        try {
            bfWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (bfWriter != null) {
                bfWriter.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
