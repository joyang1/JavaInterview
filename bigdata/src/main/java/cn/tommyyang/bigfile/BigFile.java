package cn.tommyyang.bigfile;

import cn.tommyyang.bigfile.fileservice.FileService;

import java.io.IOException;

/**
 * @author TommyYang on 2019-04-09
 * 
 * 使用说明
 * run cmd: java -cp bigdata-1.0-SNAPSHOT.jar cn.tommyyang.bigfile.BigFile
 */
public class BigFile {

    public static void main(String[] args) throws IOException {
        FileService fileService = new FileService("./smallfiles/sink-0.txt");
        //BigFileTool.readContent(args[0], args[1]);
        fileService.countWords();
        System.out.println(fileService.getTreeMap());
        System.out.println(fileService.getTreeMap().firstEntry().getKey() + ":" + fileService.getTreeMap().firstEntry().getValue());
    }

}
