import com.google.gson.Gson;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author TommyYang on 2019-04-12
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
//        testDelItemFromMap();

        List<Integer> aList = new ArrayList<>(1000);
        IntStream.range(0, 999).forEach(index -> aList.add(index));
        Set<Integer> res = new HashSet<>();
        aList.parallelStream().forEach(i -> res.add(i));
        aList.parallelStream().forEach(i -> res.add(i));

        System.out.println(res.size());
    }


    static class B {
        private int id;
        private boolean inWhiteList;

        public B(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isInWhiteList() {
            return inWhiteList;
        }

        public void setInWhiteList(boolean inWhiteList) {
            this.inWhiteList = inWhiteList;
        }

        @Override
        public String toString() {
            return "B{" +
                    "id=" + id +
                    ", inWhiteList=" + inWhiteList +
                    '}';
        }
    }

    static class A {
        private Integer customerId;

        private Integer shopId;

        private Integer moduleId;

        private Date beginTime;

        private Date endTime;

        public A(Integer customerId, Integer shopId, Integer moduleId, Date beginTime, Date endTime) {
            this.customerId = customerId;
            this.shopId = shopId;
            this.moduleId = moduleId;
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
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
