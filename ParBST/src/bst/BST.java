package bst;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//this class is the implementation of fine-grained lock binary search tree
public class BST {

    //BinarySearchTree global lock
    Lock bstLock = new ReentrantLock();

    //root node
    private Node root;

    //initialize root
    public BST() {
        root = null;
    }

    /*****************************************************
     *
     *            INSERT
     *
     ******************************************************/
    public void insert(int data) {
        //get global lock for handling root insertion
        bstLock.lock();
        //if root is null, make new node, assign, release global lock and return
        if (root == null) {
            root = new Node(data);
            bstLock.unlock();
            return;
        }

        //if root is not null, get root lock
        root.lock();

        //release global lock
        bstLock.unlock();

        //for finding a position of insertion, should have curr node and next node
        //with these two nodes, implementation fine-grained lock
        Node curr = root;
        Node next = null;

        while (true) {
            //if insertion data is smaller than curr node data, go to left
            if (data < curr.data) {
                //if left is null, that position is insertion position
                if (curr.left == null) {
                    //assign new Node, release lock and return
                    curr.left = new Node(data);
                    curr.unlock();
                    return;
                } else {
                    //if left is not null, get it as next
                    next = curr.left;
                }
            } else if (data > curr.data) {
                //if insertion data is bigger than curr node data, go to right
                //if right is null, that position is insertion position
                if (curr.right == null) {
                    //assign new Node, release lock and return
                    curr.right = new Node(data);
                    curr.unlock();
                    return;
                } else {
                    //if right is not null, get it as next
                    next = curr.right;
                }
            } else {
                curr.unlock();
                return;
            }
            //1. get next node lock
            next.lock();
            //2. release current node lock, 1 step and 2 step order is important
            curr.unlock();
            //3. go to next step
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
        //get global lock for handling root search
        bstLock.lock();
        //if root is null, there is no data, therefore, release global lock and return
        if (root == null) {
            bstLock.unlock();
            return false;
        } else {
            //find from root
            Node curr = root;

            //get current lock and release global lock
            curr.lock();
            bstLock.unlock();

            while (true) {
                //if current data is equal to search data, release current lock and return
                if (curr.data == toSearch) {
                    curr.unlock();
                    return true;
                } else if (toSearch < curr.data) {
                    //if search data is smaller than current data, go to left
                    Node next = curr.left;
                    //if next is null, there is no data equal to search data, therefore, release lock and return
                    if (next == null) {
                        curr.unlock();
                        return false;
                    }

                    //1. get next node lock
                    next.lock();
                    //2. release current node lock, 1 step and 2 step order is important
                    curr.unlock();
                    //3. go to next step
                    curr = next;
                } else if (toSearch > curr.data) {
                    //if search data is bigger than current data, go to right
                    Node next = curr.right;
                    //if next is null, there is no data equal to search data, therefore, release lock and return
                    if (next == null) {
                        curr.unlock();
                        return false;
                    }

                    //1. get next node lock
                    next.lock();
                    //2. release current node lock, 1 step and 2 step order is important
                    curr.unlock();
                    //3. go to next step
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

    //delete function is implemented at last, so it's search step can look weird
    //but same as insert and delete function
    public boolean delete(int data) {
        //get global lock for handling root delete
        bstLock.lock();
        //if root is null, there is no data, therefore, release global lock and return
        if (root == null) {
            bstLock.unlock();
        } else {
            //start from root
            Node cur = root;
            Node par;

            //get root lock
            cur.lock();

            //this part can be weird, but same as insert and search
            //if current data is bigger than search data, go to left
            //else if current data is smaller than search data, go to right
            int compare = cur.data - data;
            if (compare != 0) {
                //move to next and root save as parent
                par = cur;
                cur = compare > 0 ? cur.left : cur.right;
                //get current node lock
                cur.lock();

                //release global lock
                bstLock.unlock();

                //find delete position and delete
                while (true) {
                    compare = cur.data - data;

                    //find a node for deletion
                    if (compare == 0) {
                        //find node for replacement
                        Node rep = retrieveData(cur);

                        //compare data value and replace
                        compare = par.data - data;
                        if (compare > 0) par.left = rep;
                        else par.right = rep;

                        //if replacement data is a node, save children of current node
                        if (rep != null) {
                            rep.left = cur.left;
                            rep.right = cur.right;
                        }

                        //release current and parent lock
                        cur.unlock();
                        par.unlock();
                        break;
                    } else {
                        //do not find a node for deletion

                        //can release parent node, becaue already have current node lock
                        par.unlock();
                        par = cur;

                        //as result of comparison, get current node from children
                        compare = cur.data - data;
                        if (compare > 0) cur = cur.left;
                        else cur = cur.right;
                    }

                    //if current node is null, can't find delete data
                    if (cur == null) return false;
                        //else, get current node lock
                    else cur.lock();
                }
            } else {
                //if delete root node, find replacement data and replace
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

    //this function is for finding replacement data.
    //if left subtree is not null, find rightmost of left subtree
    //else if right subtree is not null, find leftmost of right subtree
    //else return null
    private Node retrieveData(Node sub) {
        Node cur;
        Node par = sub;

        //if left subtree exist
        if (sub.left != null) {
            cur = sub.left;

            //get left subtree lock
            cur.lock();

            //find rightmost node with fine-grained lock
            while (cur.right != null) {
                if (par != sub) {
                    //if current parent is not root of subtree
                    par.unlock();
                }
                par = cur;
                cur = cur.right;
                cur.lock();
            }

            //if left children node of rightmost node, get lock for change
            if (cur.left != null) {
                cur.left.lock();
            }

            //if parent is equal to rightmost node of subtree,
            //left children of current node is attached to parent left
            if (par == sub) par.left = cur.left;
            else {
                //except above case, left children of current node is attached to parent right
                par.right = cur.left;
                par.unlock();
            }

            //if left children node of rightmost node, release lock
            if (cur.left != null) {
                cur.left.unlock();
            }

            cur.unlock();
        } else if (sub.right != null) {
            cur = sub.right;

            //get left subtree lock

            cur.lock();

            //find leftmost node with fine-grained lock
            while (cur.left != null) {
                if (par != sub) {
                    //if current parent is not root of subtree
                    par.unlock();
                }
                par = cur;
                cur = cur.left;
                cur.lock();
            }

            //if right children node of leftmost node, get lock for change
            if (cur.right != null) {
                cur.right.lock();
            }

            //if parent is equal to leftmost node of subtree,
            //right children of current node is attached to parent right
            if (par == sub) par.right = cur.right;
            else {
                //except above case, right children of current node is attached to parent left
                par.left = cur.right;
                par.unlock();
            }

            //if right children node of leftmost node, release lock
            if (cur.right != null) {
                cur.right.unlock();
            }

            cur.unlock();
        } else {
            //there are no subtrees
            return null;
        }

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
