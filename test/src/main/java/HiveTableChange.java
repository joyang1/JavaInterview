import java.util.stream.IntStream;

/**
 * @Author : TommyYang
 * @Time : 2019-08-22 11:29
 * @Software: IntelliJ IDEA
 * @File : HiveTableChange.java
 */
public class HiveTableChange {

    //  CASCADE; add or replace column with CASCADE will update table and partition metadata;

    public static void main(String[] args) {
        StringBuilder sBuilder = new StringBuilder("ALTER TABLE hdfs.rank_user_features ADD COLUMNS (");
//        StringBuilder sBuilder = new StringBuilder("CREATE EXTERNAL TABLE `hdfs.rank_user_features`(");
        builder(sBuilder, "add");

        System.out.println(sBuilder.toString());
    }

    private static void builder(StringBuilder sBuilder, String mode) {
        if (!mode.equals("add")) {
            sBuilder.append("`batch_id` string, ").append(" `gender` int, ")
                    .append("`entity_type` string, ").append("`entity_id` string, ");

            IntStream.range(1, 86).forEach(index -> {
                String latesClickStr = String.format("`click_label%d` int", index);
                sBuilder.append(latesClickStr).append(", ");
            });

            IntStream.range(1, 86).forEach(index -> {
                String latesClickStr = String.format("`impr_label%d` int", index);
                sBuilder.append(latesClickStr).append(", ");
            });

            IntStream.range(1, 86).forEach(index -> {
                String latesClickStr = String.format("`read_label%d` int", index);
                sBuilder.append(latesClickStr).append(", ");
            });
        }

        IntStream.range(1, 86).forEach(index -> {
            String latesClickStr = String.format("`latest_click_label%d` int", index);
            sBuilder.append(latesClickStr).append(", ");
        });

        IntStream.range(1, 86).forEach(index -> {
            String latesClickStr = String.format("`latest_impr_label%d` int", index);
            sBuilder.append(latesClickStr).append(", ");
        });

        IntStream.range(1, 86).forEach(index -> {
            String latesClickStr = String.format("`latest_read_label%d` int", index);
            if (index < 85) {
                sBuilder.append(latesClickStr).append(",");
            } else {
                sBuilder.append(latesClickStr);
            }
        });

        if (mode.equals("add")){
            sBuilder.append(") CASCADE;");
        } else if (mode.equals("replace")) {
            sBuilder.append(",dt string) CASCADE;");
        } else if (mode.equals("create")) {
            sBuilder.append(")\n");
            sBuilder.append("PARTITIONED BY (`dt` string)\n")
                    .append("ROW FORMAT SERDE  'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'\n")
                    .append("WITH SERDEPROPERTIES ('field.delim'='\\t', 'serialization.format'='\\t')\n")
                    .append("STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat'\n")
                    .append("OUTPUTFORMAT  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'\n")
                    .append("LOCATION 'hdfs://ns1/data/frontpagenote_recommend_records/user_features';");
        }

    }

}
