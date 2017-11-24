package thread;

import bst.BST;

import java.util.concurrent.BlockingQueue;

public class Task implements Runnable {

    protected BlockingQueue queue = null;
    protected BST bst = null;

    @Override
    public void run() {
        int data = 0;
        while(!queue.isEmpty()){
            try {
               data = (int) queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bst.insert(data);
        }
    }

    public Task(BST bst, BlockingQueue<Integer> queue) {
        this.queue = queue;
        this.bst = bst;
    }

}
