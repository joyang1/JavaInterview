import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author TommyYang on 2019-04-12
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
//        testDelItemFromMap();

//        List<Integer> aList = new ArrayList<>(1000);
//        IntStream.range(0, 999).forEach(index -> aList.add(index));
//        Set<Integer> res = new HashSet<>();
//        aList.parallelStream().forEach(i -> res.add(i));
//        aList.parallelStream().forEach(i -> res.add(i));
//
//        System.out.println(res.size());

//        List<Integer> vals = new ArrayList<>();
//        Map<Integer, List<Long>> map = new HashMap<>(14);
//
//        for (Integer i = 0; i <= 126; i++) {
//            vals.add(i);
//        }


        List<B> bList = new ArrayList<>();
        B b1 = new B(1);
        b1.setInWhiteList(true);
        B b2 = new B(2);
        b2.setInWhiteList(true);
        B b3 = new B(3);
        b3.setInWhiteList(true);
        B b4 = new B(4);
        b4.setInWhiteList(true);
        bList.add(b1);
//        bList.add(b1);
        bList.add(b2);
        bList.add(b3);
        bList.add(b4);

        List<A> aList = new ArrayList<>();
        aList.add(new A(2, "AAA"));
        aList.add(new A(3, "AAA1"));
        aList.add(new A(5, "AAA2"));
        aList.add(new A(7, "AAA3"));
        aList.add(new A(9, "AAA4"));
        IntStream.range(10, 100).forEach(item -> aList.add(new A(item, "AAA" + item)));

        A a = aList.stream().filter(item -> item.getId() == 10).findFirst().get();
        System.out.println(a);

//        Map<Integer, String> map = new HashMap<>();
//        IntStream.range(1, 6).forEach(item -> map.put(item, "CCC" + item));
//
//        Long t1 = System.currentTimeMillis();
//
////        aList.parallelStream().forEach(a -> {
////            if (map.containsKey(a.id)) {
////                a.setValue(map.get(a.id));
////            }
////        });
//        aList.parallelStream().map(A::getValue).collect(Collectors.toList());
//
//        Long t2 = System.currentTimeMillis();
//
////        aList.stream().forEach(a -> {
////            if (map.containsKey(a.id)) {
////                a.setValue(map.get(a.id));
////            }
////        });
//        aList.stream().map(A::getValue).collect(Collectors.toList());
//
//        Long t3 = System.currentTimeMillis();
//
//        aList.forEach(a -> {
//            if (map.containsKey(a.id)) {
//                a.setValue(map.get(a.id));
//            }
//        });
//        Long t4 = System.currentTimeMillis();
//
//        System.out.println(aList);
//
//        System.out.println("parallelStream:" + (t2 - t1));
//        System.out.println("stream:" + (t3 - t2));
//        System.out.println("direct foreach:" + (t4 - t3));
//
//
//       List<String> list =  Lists.newArrayList("288867","286818","280552","253830","253813","253811","253808","253805","253803","253802","243284","229414","229411","228492","194795","193587","193586","193003","168281","131257","125441","124735","117672","117670","117669","117668","117666","117665","117663","117661","117646","117645","117644","117643","108262","105711","97446","97445","86982","75899","75897","75895","75894","75892","75890","75889","75887","75885","75882","75880","75876","75874","75869","75865","75864","75863","75861","75860","75858","75855","75854","75853","75852","75851","75850","75849","75846","75842","75840","75838","75837","75836","75834","75832","75831","75830","75828","75826","75825","75824","75822","75821","75820","75819","75818","75816","75815","75814","75813","75812","75811","75810","75809","75808","75807","75806","75805","75804","75803","75802","75801","75800","75799","75798","75797","75796","75795","75794","75793","75792","75791","75790","75787","1");
//       System.out.println(JSONObject.toJSONString(list));
//       System.out.println(list.size());

        List<Integer> valList = Lists.newArrayList(

        );
        StringBuilder stringBuilder = new StringBuilder("[\"java.util.ArrayList\", [\n");
        for (Integer val : valList) {
            stringBuilder.append("[\"java.lang.Long\", " + val + "],\n");
        }
        stringBuilder.append("]]");
        System.out.println(stringBuilder);

//        try (Stream<String> stream = Files.lines(Paths.get("/Users/mtdp/aa.md"))) {
//            StringBuilder sb = new StringBuilder();
//            stream.forEach(line -> sb.append(line).append(",\n"));
//
//            System.out.println(sb.toString());
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
        private Integer id;

        private String value;

        public A(Integer id, String value) {
            this.id = id;
            this.value = value;
        }

        public Integer getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "A{" +
                    "id=" + id +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static void testDelItemFromMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "aa");
        map.put("bb", "bb");
        map.put("cc", "cc");
        map.put("dd", "dd");

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> val = it.next();
            if (val.getValue().equals("aa")) {
                it.remove();
            }
        }

        System.out.println(map);

    }

}
