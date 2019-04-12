package cn.tommyyang.bigfile.fileservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.TreeMap;

/**
 * @author TommyYang on 2019-04-12
 */
public class FileService {

    private String fileName;

    private TreeMap<String, Long> treeMap;

    public FileService(String fileName) {
        this.fileName = fileName;
        this.treeMap = new TreeMap<>();
    }

    public TreeMap<String, Long> getTreeMap() {
        return treeMap;
    }

    public void countWords() throws IOException {
        File file = new File(this.fileName);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel fc = inputStream.getChannel();

        ByteBuffer bf = ByteBuffer.allocate(10 * 1024 * 1024);

        int len = -1;
        while ((len = fc.read(bf)) != -1){
            bf.clear();
            byte[] bytes = bf.array();
            String values = new String(bytes, 0 , len);
            String[] valArr = values.split(System.lineSeparator());
            for (String val : valArr){
                Long cnt = this.treeMap.get(val);
                if (cnt == null){
                    this.treeMap.put(val, 0l);
                }else {
                    this.treeMap.put(val, ++cnt);
                }
            }
        }
    }
}
