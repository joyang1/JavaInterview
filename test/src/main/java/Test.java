import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author TommyYang on 2019-04-12
 */
public class Test {

    public static void main(String[] args){
        testDelItemFromMap();
    }


    public static void testDelItemFromMap(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "aa");
        map.put("bb", "bb");
        map.put("cc", "cc");
        map.put("dd", "dd");

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> val = it.next();
            if (val.getValue().equals("aa")){
                it.remove();
            }
        }

        System.out.println(map);

    }

}
