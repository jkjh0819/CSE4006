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
        if (root == null) {
            bstLock.unlock();
            return false;
        } else {
            Node curr = root;
            curr.lock();
            bstLock.unlock();

            while (true) {
                if (curr.data == toSearch) {
                    curr.unlock();
                    return true;
                } else if (toSearch < curr.data ) {
                    Node next = curr.left;
                    if (next == null) {
                        curr.unlock();
                        return false;
                    }

                    next.lock();
                    curr.unlock();
                    curr = next;
                } else if (toSearch > curr.data) {
                    Node next = curr.right;
                    if (next == null) {
                        curr.unlock();
                        return false;
                    }

                    next.lock();
                    curr.unlock();
                    curr = next;
                }
            }
        }
    }

    /*****************************************************
     *
     *            DELETE
     *
     ******************************************************/

    public boolean delete(int data) {
        bstLock.lock();
        if (root == null) {
            bstLock.unlock();
        } else {
            Node cur = root;
            Node par;
            cur.lock();

            int compare = cur.data - data;
            if (compare != 0) {
                par = cur;
                cur = compare > 0 ? cur.left : cur.right;
                cur.lock();
                bstLock.unlock();

                while (true) {
                    compare = cur.data - data;
                    if (compare == 0) {
                        Node rep = retrieveData(cur);

                        compare = par.data - data;
                        if (compare > 0) par.left = rep;
                        else par.right = rep;

                        if (rep != null) {
                            rep.left = cur.left;
                            rep.right = cur.right;
                        }

                        cur.unlock();
                        par.unlock();
                        break;
                    } else {
                        par.unlock();
                        par = cur;

                        compare = cur.data - data;
                        if (compare > 0) cur = cur.left;
                        else cur = cur.right;
                    }

                    if (cur == null) return false;
                    else cur.lock();
                }
            } else {
                Node rep = retrieveData(cur);
                root = rep;

                if (rep != null) {
                    rep.left = cur.left;
                    rep.right = cur.right;
                }

                cur.unlock();
                bstLock.unlock();
            }

        }
        return true;
    }

    private Node retrieveData(Node sub) {
        Node cur;
        Node par = sub;

        if (sub.left != null) {
            cur = sub.left;
            cur.lock();

            while (cur.right != null) {
                if (par != sub) par.unlock();
                par = cur;
                cur = cur.right;
                cur.lock();
            }

            if (cur.left != null) cur.left.lock();
            if (par == sub) par.left = cur.left;
            else {
                par.right = cur.left;
                par.unlock();
            }
            if (cur.left != null) cur.left.unlock();

            cur.unlock();
        } else if (sub.right != null) {
            cur = sub.right;
            cur.lock();

            while (cur.left != null) {
                if (par != sub) par.unlock();
                par = cur;
                cur = cur.left;
                cur.lock();
            }

            if (cur.right != null) cur.right.lock();
            if (par == sub) par.right = cur.right;
            else {
                par.left = cur.right;
                par.unlock();
            }
            if (cur.right != null) cur.right.unlock();

            cur.unlock();
        } else return null;
        return cur;
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
