import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @Author : TommyYang
 * @Time : 2019-09-17 16:23
 * @Software: IntelliJ IDEA
 * @File : StringUitls.java
 */
public class StringUitls {

    public static void main(String[] args) throws IOException {
        Stream<String> lines = Files.lines(Paths.get("/Users/tommy/Documents/aa.txt"), Charset.defaultCharset());
        lines.forEach(line -> {
//            System.out.println(line.trim());
            int start = line.trim().indexOf(">");
            int end = line.trim().lastIndexOf("<");
            String value = line.trim().substring(start + 1, end);
            line = line.trim().replace("<option>", String.format("<option value=\"%s\">", value));
            System.out.println(line);
        });
    }

}
