import lf_ll.LFLinkedList;

public class Program {
    public static void main(String[] args) {

        
        Integer[] a = {1, 5, 2, 7, 4, 10, 15, 11, 13, 20, 9};
        LFLinkedList lfll = new LFLinkedList();
        for (Integer n : a) {
            lfll.add(n);
        }

        for(Integer n : a){
            System.out.println(lfll.search(n));
        }
    }
}
