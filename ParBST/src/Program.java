import bst.BST;
import thread.Task;

import java.util.Random;
import java.util.concurrent.*;

public class Program {
    public static BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
    public static BST bst = new BST();

    public static void main(String[] args) {
        Random random = new Random();

        for (int i = 0; i < 1000000; ++i) {
            queue.add(random.nextInt(1000000));
        }

        ExecutorService execService = Executors.newFixedThreadPool(2);

        execService.execute(new Task(bst, queue));
        execService.execute(new Task(bst, queue));

        execService.shutdown();
        try {
            execService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // bst.inOrderTraversal();
        // System.out.println();

        bst.delete(1);
        System.out.println("min:" + bst.findMin());

        bst.delete(5);
        System.out.println("min:" + bst.findMin());

        bst.delete(2);
        System.out.println("min:" + bst.findMin());

        bst.delete(15);
        System.out.println("min:" + bst.findMin());

        bst.delete(10);
        System.out.println("min:" + bst.findMin());

        bst.delete(4);
        System.out.println("min:" + bst.findMin());

        //   bst.inOrderTraversal();
        // System.out.println();

        bst.delete(7);
        System.out.println("min:" + bst.findMin());

        bst.delete(9);
        System.out.println("min:" + bst.findMin());

        //bst.inOrderTraversal();
        System.out.println();

    }
}
