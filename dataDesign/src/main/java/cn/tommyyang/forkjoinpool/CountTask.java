package cn.tommyyang.forkjoinpool;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author TommyYang on 2019-04-23
 */
public class CountTask extends RecursiveTask<Integer> {

    private final static int THRESHOLD = 10000;

    private List<Integer> list;
    private int start;
    private int end;

    public CountTask(List<Integer> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        boolean canCompute = (this.end - this.start) <= THRESHOLD;
        if (canCompute) {
            System.out.println(this.start + "--" + this.end);
            for (int i = this.start; i <= this.end; i++){
                sum += this.list.get(i);
            }
            System.out.println(sum);
        } else {
            int mid = (this.start + this.end) / 2;
            CountTask leftTask = new CountTask(this.list, this.start, mid);
            CountTask rightTask = new CountTask(this.list, mid + 1, this.end);

            leftTask.fork();
            rightTask.fork();

            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            sum = leftResult + rightResult;
        }

        return sum;
    }




}
