import bst.BST;

import java.util.Random;

public class Program {
    public static void main(String[] args)
    {
        Random random = new Random();
        thread.Pool pool = new thread.Pool(2);
        BST bst = new BST();

        for (int i = 0; i < 1000000; ++i) {
            pool.push(() -> bst.insert(random.nextInt(1000000)));
        }
        pool.join();


       // bst.inOrderTraversal();
       // System.out.println();

        bst.delete(1);
        System.out.println("min:"+bst.findMin());

        bst.delete(5);
        System.out.println("min:"+bst.findMin());

        bst.delete(2);
        System.out.println("min:"+bst.findMin());

        bst.delete(15);
        System.out.println("min:"+bst.findMin());

        bst.delete(10);
        System.out.println("min:"+bst.findMin());

        bst.delete(4);
        System.out.println("min:"+bst.findMin());

     //   bst.inOrderTraversal();
       // System.out.println();

        bst.delete(7);
        System.out.println("min:"+bst.findMin());

        bst.delete(9);
        System.out.println("min:"+bst.findMin());

        bst.inOrderTraversal();
        System.out.println();

    }
}
