package cn.tommyyang;

import cn.tommyyang.forkjoinpool.CountTask;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019-04-12
 */
public class RunTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        runForkJoinPool();

        Thread t = new Thread(()->
            System.out.println("aa")
        );

        t.start();
        t.start();
    }

    private static void runForkJoinPool() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        List<Integer> arrs = new ArrayList<>(100000);
        for (int i = 1; i <= 100000; i++) {
            arrs.add(i);
        }

        CountTask countTask = new CountTask(arrs, 0, arrs.size() - 1);

        Future<Integer> r1 = forkJoinPool.submit(countTask);
        System.out.println(r1.get());
        ExecutorService service = Executors.newWorkStealingPool(1);
        service.execute(() -> System.out.println("aa"));
//        ThreadPoolExecutor
//        Math
//        LinkedBlockingQueue
//        HashMap

    }
}
