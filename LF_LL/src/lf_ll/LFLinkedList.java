package lf_ll;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LFLinkedList {

    private Node head, tail;

    public LFLinkedList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = new AtomicMarkableReference<>(tail, false);
        tail.next = new AtomicMarkableReference<>(head, false);
    }

    public boolean add(int data) {
        while (true) {
            Window window = find(head, data);
            Node pred = window.pred, curr = window.curr;

            if (curr.data == data) {
                return false;
            } else {
                Node node = new Node(data);
                node.next = new AtomicMarkableReference<>(curr, false);
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    public boolean remove(int data) {
        boolean tryNode;
        while (true) {
            Window window = find(head, data);
            Node pred = window.pred, curr = window.curr;
            if (curr.data != data) {
                return false;
            } else {
                Node succ = curr.next.getReference();
                tryNode = curr.next.attemptMark(succ, true);
                if (!tryNode)
                    continue;
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }

    public boolean search(int data){
        boolean[] deleteBit = {false};
        Node curr = head;
        while(curr.data < data){
            curr = curr.next.getReference();
            Node succ = curr.next.get(deleteBit);
        }
        return (curr.data == data && !deleteBit[0]);
    }

    public Window find(Node head, int data) {
        Node pred = null, curr = null, succ = null;
        boolean[] deleteBit = {false};
        boolean tryNode;
        retry:
        while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(deleteBit);
                while (deleteBit[0]) {
                    tryNode = pred.next.compareAndSet(curr, succ, false, false);
                    if (!tryNode) continue retry;
                    curr = succ;
                    succ = curr.next.get(deleteBit);
                }
                if (curr.data >= data)
                    return new Window(pred, curr);
                pred = curr;
                curr = succ;
            }
        }
    }
}

