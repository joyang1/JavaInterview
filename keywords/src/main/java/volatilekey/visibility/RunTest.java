package volatilekey.visibility;

/**
 * @author TommyYang on 2019/1/29
 */
public class RunTest extends Thread {

    //volatile保证线程的可见性
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
        RunTest voatileTest = new RunTest();
        voatileTest.start();

        Thread.sleep(1000);

        voatileTest.setRunning(false);
    }
}
