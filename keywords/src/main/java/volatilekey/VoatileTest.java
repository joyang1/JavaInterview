package volatilekey;

/**
 * @author TommyYang on 2019/1/29
 */
public class VoatileTest extends Thread {

    //private volatile boolean isRunning = true;
    private boolean isRunning = true;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void run(){

        while (isRunning()){
        }
        System.out.println("not running");
    }

    public static void main(String[] args) throws InterruptedException {
        VoatileTest voatileTest = new VoatileTest();
        voatileTest.start();

        Thread.sleep(1000);

        voatileTest.setRunning(false);
    }
}
