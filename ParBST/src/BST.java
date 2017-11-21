/** **************************************************************************
 *  The sequential Binary Search Tree (for storing int values)
 *
 *****************************************************************************/

import java.util.*;

public class BST {
   public static void main(String[] args)
   {
      Integer[] a = {1,5,2,7,4, 10, 15, 11, 13, 20, 9};
      BST bst = new BST();
      for(Integer n : a) {
          bst.insert(n);
      }

      bst.inOrderTraversal();
      System.out.println();

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

      bst.inOrderTraversal();
      System.out.println();

      bst.delete(7);  
      System.out.println("min:"+bst.findMin());

      bst.delete(9);  
      System.out.println("min:"+bst.findMin());

      bst.inOrderTraversal();
      System.out.println();
   }

   private Node root;

   public BST()
   {
      root = null;
   }

/*****************************************************
*
*            INSERT
*
******************************************************/
   public void insert(int data)
   {
      root = insert(root, data);
   }
   private Node insert(Node p, int toInsert)
   {
      if (p == null)
         return new Node(toInsert);

      if (toInsert == p.data)
      	return p;

      if (toInsert < p.data)
         p.left = insert(p.left, toInsert);
      else
         p.right = insert(p.right, toInsert);

      return p;
   }

/*****************************************************
*
*            SEARCH
*
******************************************************/

   // you don't need to implement hand-over-hand lock for this function.
   public int findMin() 
   {
       if (root == null) {
           throw new RuntimeException("cannot findMin.");
       }
       Node n = root;
       while (n.left != null) {
           n = n.left;
       }
       return n.data;
   }

   public boolean search(int toSearch)
   {
      return search(root, toSearch);
   }
   private boolean search(Node p, int toSearch)
   {
      if (p == null)
         return false;
      else
      if (toSearch == p.data)
      	return true;
      else
      if (toSearch < p.data)
         return search(p.left, toSearch);
      else
         return search(p.right, toSearch);
   }

/*****************************************************
*
*            DELETE
*
******************************************************/

   public boolean delete(int toDelete)
   {
      try {
          root = delete(root, toDelete);
          return true;
      } catch (RuntimeException e) {
          return false;
      }
   }
   private Node delete(Node p, int toDelete)
   {
      if (p == null) {
          throw new RuntimeException("cannot delete.");
      } else if (toDelete < p.data) {
        p.left = delete (p.left, toDelete);
      } else if (toDelete > p.data) {
        p.right = delete (p.right, toDelete);
      } else {
         if (p.left == null) { return p.right; }
         else if (p.right == null) { return p.left;}
         else {
         // get data from the rightmost node in the left subtree
            p.data = retrieveData(p.left);
         // delete the rightmost node in the left subtree
            p.left =  delete(p.left, p.data) ;
         }
      }
      return p;
   }
   private int retrieveData(Node p)
   {
      while (p.right != null) {
          p = p.right;
      }
      return p.data;
   }

/*************************************************
 *
 *            TRAVERSAL
 *
 **************************************************/

   public void preOrderTraversal()
   {
      preOrderHelper(root);
   }
   private void preOrderHelper(Node r)
   {
      if (r != null)
      {
         System.out.print(r+" ");
         preOrderHelper(r.left);
         preOrderHelper(r.right);
      }
   }

   public void inOrderTraversal()
   {
      inOrderHelper(root);
   }
   private void inOrderHelper(Node r)
   {
      if (r != null)
      {
         inOrderHelper(r.left);
         System.out.print(r+" ");
         inOrderHelper(r.right);
      }
   }

   private class Node
   {
      private int data;
      private Node left, right;

      public Node(int data, Node l, Node r)
      {
         left = l; right = r;
         this.data = data;
      }

      public Node(int data)
      {
         this(data, null, null);
      }

      public String toString()
      {
         return ""+data;
      }
   } 
}
