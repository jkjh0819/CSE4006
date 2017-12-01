package lf_ll;

import java.util.concurrent.atomic.AtomicMarkableReference;

//this class is the implementation of lock-free linked list
public class LFLinkedList {

    //head and tail of linked list
    private Node head, tail;

    public LFLinkedList() {
        //set head as minimum integer and tail as maximum integer
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);

        //connect head and tail for avoid nullPointerException at first
        head.next = new AtomicMarkableReference<>(tail, false);
        tail.next = new AtomicMarkableReference<>(head, false);
    }

    //this function is addition of new node to linked list
    public boolean add(int data) {
        while (true) {
            //get snap(targets of operation)
            Snap snap = getSnap(head, data);

            //and set snap value to logical variable
            Node pred = snap.pred, curr = snap.curr;

            //new data is already in linked list, return false
            //do not accept duplicate
            if (curr.data == data) {
                return false;
            } else {
                //make new node
                Node node = new Node(data);
                node.next = new AtomicMarkableReference<>(curr, false);

                //try atomic operation and get return value
                //if return value is true, addition is complete, else retry
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    //this function is remove of node from linked list
    public boolean remove(int data) {
        boolean tryMark;
        while (true) {
            //get snap(targets of operation)
            Snap snap = getSnap(head, data);

            //and set snap value to logical variable
            Node pred = snap.pred, curr = snap.curr;

            //if there is no data for remove in linked list, return false
            if (curr.data != data) {
                return false;
            } else {
                //get next node reference of remove node
                Node succ = curr.next.getReference();

                //set logical delete bit as true to next reference of current node
                tryMark = curr.next.attemptMark(succ, true);

                //if marking do not success, retry
                if (!tryMark)
                    continue;

                //set successor reference to predecessor next reference
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }

    //this function is search of node in linked list
    public boolean search(int data){
        //initialize check array
        boolean[] deleteBit = {false};

        //start from head
        Node curr = head;

        //this linked list is sorted, so run as current data is smaller than search data
        while(curr.data < data){
            //get next node reference
            curr = curr.next.getReference();
            //if current is not in logical deletion, deleteBit is false and can get next
            //but, is in logical deletion, deleteBit is true
            Node succ = curr.next.get(deleteBit);
        }

        //if current node data is equal to data and the node is not in logical deletion
        return (curr.data == data && !deleteBit[0]);
    }

    //this function is for finding targets of a operation
    public Snap getSnap(Node head, int data) {
        Node pred = null, curr = null, succ = null;

        //initialize check array
        boolean[] deleteBit = {false};
        boolean trySet;
        retry:
        while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                //if successor is not in logical deletion, deleteBit is false
                succ = curr.next.get(deleteBit);

                //if deleteBit is true, its connection should be cut
                while (deleteBit[0]) {
                    //try connection cut by atomic operation
                    trySet = pred.next.compareAndSet(curr, succ, false, false);

                    //if atomic operation failed, should search and retry from head
                    if (!trySet) {
                        //label is anti-pattern about readability, but at this algorithm, label is more understandable
                        continue retry;
                    }

                    //else, do connection cut next from successor if logical deletion was executed
                    curr = succ;
                    succ = curr.next.get(deleteBit);
                }
                //getSnap target
                if (curr.data >= data)
                    return new Snap(pred, curr);

                //search next
                pred = curr;
                curr = succ;
            }
        }
    }
}

