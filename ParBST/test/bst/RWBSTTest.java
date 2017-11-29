package bst;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thread.Pool;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

//test for proof operation of read write lock binary search tree
public class RWBSTTest {

    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    private static RWBST tree;
    private static Pool pool;

    @BeforeClass
    public static void init() throws Exception {
        //numbers is a collection of numbers between 0 to testSize - 1
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
    }

    //parallel test will be executed with 4 threads
    @Before
    public void prepareTest() throws Exception {
        tree = new RWBST();
        pool = new Pool(4);
    }

    //insert testSize numbers with one thread(main)
    //after insert, the numbers should be found by search(return true)
    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        numbers.forEach((e) -> assertTrue(tree.search(e)));
    }

    //insert test with 4 threads
    @Test
    public void insertParallel() throws Exception {
        numbers.forEach((e) -> pool.push(() -> tree.insert(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(tree.search(e)));
    }

    //insert testSize numbers with one thread(main)
    //after insert, the numbers shuffled and should be found by search(return true)
    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.search(e));
    }

    //search test with 4 threads
    @Test
    public void searchParallel() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(tree.search(e))));
        pool.join();
    }
}