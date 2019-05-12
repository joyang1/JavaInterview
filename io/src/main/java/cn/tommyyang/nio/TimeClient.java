package cn.tommyyang.nio;

/**
 * @author TommyYang on 2019-05-09
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8000;
        if (args != null && args.length == 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("param is error, please set port of time client!!!");
            }
        }
        for(int i = 0; i < 10; i++)
            new Thread(new TimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
    }
}
