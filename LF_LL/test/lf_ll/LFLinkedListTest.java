package lf_ll;

import lf_ll.LFLinkedList;
import thread.Pool;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

//test for proof operation of lock-free linked list
public class LFLinkedListTest {

    private static final int testSize = 30000;
    private static List<Integer> numbers;

    private static LFLinkedList lfll;
    private static Pool pool;

    @BeforeClass
    public static void init() throws Exception {
        //numbers is a collection of numbers between 0 to testSize - 1
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
    }

    //parallel test will be executed with 4 threads
    @Before
    public void prepareTest() throws Exception {
        lfll = new LFLinkedList();
        pool = new Pool(4);
    }

    //add testSize numbers with one thread(main)
    //after add, the numbers should be found by search(return true)
    @Test
    public void add() throws Exception {
        numbers.forEach((e) -> pool.push(() -> lfll.add(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(lfll.search(e)));
    }

    //remove testSize numbers with one thread(main)
    //after remove, the numbers should not be found by search(return false)
    @Test
    public void remove() throws Exception {
        numbers.forEach((e) -> lfll.add(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> lfll.remove((Integer) e)));
        pool.join();
        numbers.forEach((e) -> assertFalse(lfll.search(e)));
    }

    //add testSize numbers with one thread(main)
    //after add, the numbers shuffled and should be found by search(return true)
    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> lfll.add(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(lfll.search(e))));
        pool.join();
    }

    //4 threads execute random operation among insert, delete, search about a unique number
    //the result will be same with set operation, because a operation executed to a unique number
    @Test
    public void testAll() throws Exception {
        Set<Integer> verifier = new HashSet<>();
        Random random = new Random(0);
        numbers.forEach((e) -> {
            lfll.add(e);
            verifier.add(e);
        });
        Collections.shuffle(numbers);
        for (int i = 0; i < testSize; ++i) {
            int finalI = i;
            switch (random.nextInt(3)) {
                case 0:
                    pool.push(() -> lfll.add(numbers.get(finalI)));
                    verifier.add(numbers.get(finalI));
                    break;
                case 1:
                    pool.push(() -> lfll.remove(numbers.get(finalI)));
                    verifier.remove(numbers.get(finalI));
                    break;
                case 2:
                    pool.push(() -> lfll.search(numbers.get(finalI)));
                    break;
            }
        }
        pool.join();
        numbers.forEach((e) -> assertEquals(verifier.contains(e), lfll.search(e)));
    }
}