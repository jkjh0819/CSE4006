package bst; /**
 * *************************************************************************
 * The sequential Binary Search Tree (for storing int values)
 *****************************************************************************/

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BST {
    Lock bstLock = new ReentrantLock();

    private Node root;

    public BST() {
        root = null;
    }

    /*****************************************************
     *
     *            INSERT
     *
     ******************************************************/
    public void insert(int data) {
        bstLock.lock();
        if (root == null) {
            root = new Node(data);
            bstLock.unlock();
            return;
        }

        root.lock();
        bstLock.unlock();

        Node curr = root;
        Node next = null;

        while (true) {
            if (data < curr.data) {
                if (curr.left == null) {
                    curr.left = new Node(data);
                    curr.unlock();
                    return;
                } else {
                    next = curr.left;
                }
            } else if (data > curr.data) {
                if (curr.right == null) {
                    curr.right = new Node(data);
                    curr.unlock();
                    return;
                } else {
                    next = curr.right;
                }
            } else {
                curr.unlock();
                return;
            }
            next.lock();
            curr.unlock();
            curr = next;
        }
    }


    /*****************************************************
     *
     *            SEARCH
     *
     ******************************************************/

    // you don't need to implement hand-over-hand lock for this function.
    public int findMin() {
        bstLock.lock();
        if (root == null) {
            throw new RuntimeException("cannot findMin.");
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }

        bstLock.unlock();
        return n.data;
    }

    public boolean search(int toSearch) {
        bstLock.lock();
        boolean ret = search(root, toSearch);
        bstLock.unlock();
        return ret;
    }

    private boolean search(Node p, int toSearch) {
        if (p == null)
            return false;
        else if (toSearch == p.data)
            return true;
        else if (toSearch < p.data)
            return search(p.left, toSearch);
        else
            return search(p.right, toSearch);
    }

    /*****************************************************
     *
     *            DELETE
     *
     ******************************************************/

    public boolean delete(int toDelete) {
        bstLock.lock();
        if(root == null){
            bstLock.unlock();
            throw new RuntimeException("cannot delete.");
        } else {
            Node curr = root;
            Node parent;
            curr.lock();

            if(toDelete != curr.data){
                parent = curr;
                if(toDelete < curr.data) {
                    curr = curr.left;
                } else {
                    curr = curr.right;
                }
                curr.lock();
                bstLock.unlock();

                while(true) {
                    if(toDelete == curr.data){
                        Node pred = retrieveData(curr);

                        if(toDelete < parent.data){
                            parent.left = pred;
                        } else {
                            parent.right = pred;
                        }

                        if(pred != null){
                            pred.left = curr.left;
                            pred.right = curr.right;
                        }

                        curr.unlock();
                        parent.unlock();
                        break;
                    } else {
                        parent.unlock();
                        parent = curr;

                        if(toDelete < curr.data){
                            curr = curr.left;
                        } else {
                            curr = curr.right;
                        }
                    }

                    if(curr == null) {
                        return false;
                    } else {
                        curr.lock();
                    }

                }


            } else {
                Node pred = retrieveData(curr);
                root = pred;

                if(pred != null){
                    pred.left = curr.left;
                    pred.right = curr.right;
                }

                curr.unlock();
                bstLock.unlock();
            }
        }
        return true;
    }

    private Node retrieveData(Node subTree) {
        Node curr;
        Node parent = subTree;

        //if left of subtree is not null
        if(parent.left != null){
            curr = parent.left;
            curr.lock();

            //find predecessor
            while(curr.right != null){
                //shouldn't unlock root of subTree
                if(parent != subTree){
                    parent.unlock();
                }
                parent = curr;
                curr = curr.right;
                curr.lock();
            }

            //if predecessor has child
            if(curr.left != null) curr.left.lock();

            //if there is no right child at deleting node
            if(parent == subTree) {
                parent.left = curr.left;
            } else {
                parent.right = curr.left;
                parent.unlock();
            }

            if(curr.left != null){
                curr.left.unlock();
            }

            curr.unlock();

        } else if (parent.right != null) {
            //there is no left child in subtree
            curr = parent.right;
            curr.lock();

            //find predecessor
            while (curr.left != null) {
                //shouldn't unlock root of subtree
                if(parent != subTree){
                    parent.lock();
                }
                parent = curr;
                curr = curr.left;
                curr.lock();
            }

            //if predecessor has child
            if(curr.right != null) curr.right.lock();

            //if there is no left child at deleting node
            if(parent == subTree) {
                parent.right = curr.right;
            } else {
                parent.left = curr.right;
                parent.unlock();
            }

            if(curr.right != null) curr.right.unlock();

            curr.unlock();
        } else {
            //there is no child of subtree
            return null;
        }

        return curr;
    }


    /*************************************************
     *
     *            TRAVERSAL
     *
     **************************************************/

    public void preOrderTraversal() {
        preOrderHelper(root);
    }

    private void preOrderHelper(Node r) {
        if (r != null) {
            System.out.print(r + " ");
            preOrderHelper(r.left);
            preOrderHelper(r.right);
        }
    }

    public void inOrderTraversal() {
        inOrderHelper(root);
    }

    private void inOrderHelper(Node r) {
        if (r != null) {
            inOrderHelper(r.left);
            System.out.print(r + " ");
            inOrderHelper(r.right);
        }
    }

    private class Node {
        private int data;
        private Node left, right;
        private Lock lock = new ReentrantLock();

        public Node(int data, Node l, Node r) {
            left = l;
            right = r;
            this.data = data;
        }

        public Node(int data) {
            this(data, null, null);
        }

        public String toString() {
            return "" + data;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
