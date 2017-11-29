package thread;

import java.util.concurrent.LinkedBlockingQueue;

/*
   this ThreadPool class is from hanyang.portal QnA of CSE4006
   It is made by BaeJiun
 */

public class Pool {
    //termination condition, if all of jobs are finished, thread pool will be terminated
    private boolean terminate;

    //workers are threads
    private final Worker[] workers;

    //job queue
    private final LinkedBlockingQueue<Runnable> queue;

    public Pool(int nThreads) {
        //make nThreads threads
        workers = new Worker[nThreads];

        //initialize queue and termination condition
        queue = new LinkedBlockingQueue<>();
        terminate = false;

        //start threads
        for (int i = 0; i < nThreads; ++i) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    //push job to queue
    public void push(Runnable task) {
        synchronized (queue) {
            //this should be synchronize because all threads watched this
            queue.add(task);

            //job creation should be notified to a thread
            queue.notify();
        }
    }

    //wait that all of job finish
    public void join() {
        terminate = true;
        for (Worker worker : workers)
            try {
                //wait for completion of all worker threads
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    //return job queue size
    public final int size() {
        return queue.size();
    }

    //return whether job queue is empty
    public final boolean isEmpty() {
        return queue.isEmpty();
    }

    //New class for definition of worker thread
    private class Worker extends Thread {
        public void run() {
            Runnable task;

            while (true) {
                synchronized (queue) {
                    //get new job from queue
                    while (queue.isEmpty()) {
                        try {
                            //if queue is empty and termination condition is true, exit
                            if (queue.isEmpty() && terminate)
                                return;
                            //else, wait for job
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //if job queue is not empty, get a job
                    task = queue.poll();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}