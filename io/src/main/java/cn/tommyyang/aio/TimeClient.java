package cn.tommyyang.aio;

/**
 * @author TommyYang on 2019-05-21
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8000;
        if (args != null && args.length > 1) {
            port = Integer.valueOf(args[0]);
        }
        for (int i = 0; i < 10; i++)
            new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClient-001").start();
    }

}
