package cn.tommyyang.bigfile.Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author TommyYang on 2019-04-09
 */
public class BigFileTool {

    public static void readContent(String filePath, String sinkDir) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(8092);
        int length = -1;
        while ((length = channel.read(buffer)) != -1){
            buffer.clear();

            byte[] bytes = buffer.array();
            String values = new String(bytes, 0 , length);

            String[] valArr = values.split(" ");
            for (String val : valArr){
                int code = Math.abs(val.hashCode()) % 10;
                String txtName = "sink-" + code + ".txt";
                String sinkPath = sinkDir + txtName;
                writeContent(sinkPath, Charset.forName("utf-8").encode(val + System.lineSeparator()));
            }
        }
    }

    public static void writeContent(String sinkPath, ByteBuffer bf) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File(sinkPath), true);
        FileChannel channel = outputStream.getChannel();
        channel.write(bf);
        channel.close();
        outputStream.close();
    }

}
