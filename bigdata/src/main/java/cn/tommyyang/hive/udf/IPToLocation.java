package cn.tommyyang.hive.udf;

import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;
import net.ipip.ipdb.IPFormatException;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @Author : TommyYang
 * @Time : 2019-08-30 16:53
 * @Software: IntelliJ IDEA
 * @File : IPToLocation.java
 */
@Description(
        name = "ip2loc",
        value = "_FUNC_(str) - covert ip to location",
        extended = "Example:\n" +
                "  > SELECT ip2loc(ip) FROM ips;\n" +
                "  [中国,上海]"
)
public class IPToLocation extends UDF {

    private static final InputStream stream = IPToLocation.class.getClassLoader().getResourceAsStream("ipipfree.ipdb");
    private static City db = null;

    static {
        try {
            db = new City(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 具体实现逻辑
    public ArrayList<String> evaluate(Text s) throws IOException, IPFormatException {
        ArrayList<String> allTexts = new ArrayList<>();
        if (s != null) {
            CityInfo info = db.findInfo(s.toString(), "CN");
            allTexts.add(info.getCountryName());
            allTexts.add(info.getRegionName());
            allTexts.add(info.getCityName());
        }
        return allTexts;
    }

}