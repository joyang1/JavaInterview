package cn.tommyyang.aio;

/**
 * @author TommyYang on 2019-05-21
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8000;
        if (args != null && args.length == 1){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e){
                e.printStackTrace();
                System.out.println("param is error, please set port of time server!!!");
            }
        }
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);

        new Thread(timeServerHandler, "AIO-AsyncTimeServer-001").start();
    }

}
