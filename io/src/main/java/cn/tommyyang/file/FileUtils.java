package cn.tommyyang.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @Author : TommyYang
 * @Time : 2019-09-09 16:36
 * @Software: IntelliJ IDEA
 * @File : FileUtils.java
 */
public class FileUtils {

    public static synchronized void writeFile(String filePath, String content) {
        FileOutputStream outStream = null;
        BufferedWriter bfWriter = null;
        try {
            outStream = new FileOutputStream(filePath, true);
            bfWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            bfWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfWriter != null) {
                    bfWriter.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {

            }
        }
    }

}
