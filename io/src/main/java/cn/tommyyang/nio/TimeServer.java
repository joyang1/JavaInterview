package cn.tommyyang.nio;

/**
 * @author TommyYang on 2019-05-09
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8000;
        if (args != null && args.length == 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("param is error, please set port of time server!!!");
            }
        }

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }

}
