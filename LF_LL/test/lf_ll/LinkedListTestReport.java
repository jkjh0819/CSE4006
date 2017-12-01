package lf_ll;

import thread.Pool;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//test for report of lock-free linked list
@RunWith(Parameterized.class)
public class LinkedListTestReport {

    private int numThreads;
    private int[] searchRatio;
    private static final int testSize = 100000;
    private static List<Integer> numbers;

    private LFLinkedList lfll;
    private Pool pool;

    @BeforeClass
    public static void init() throws Exception {
        //numbers is a collection of numbers between 0 to testSize - 1
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
    }

    //parallel test will be executed with n threads
    @Before
    public void prepareTest() throws Exception {
        lfll = new LFLinkedList();
        pool = new Pool(numThreads);
    }

    //this will be numThreads parameter
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1}, {2}, {4}, {8}
        });
    }

    //initialize parametrized class
    public LinkedListTestReport(int numThreads) {
        this.numThreads = numThreads;
        this.searchRatio = new int[]{1, 4, 9};
    }

    //calculate execution time
    private static long calcRunTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - start;
    }

    //decide whether insert operation or search operation
    private static boolean assertRatio(int standard, int ratio, int value) {
        return (value % (standard + ratio)) < standard;
    }

    //insert testSize numbers with n threads
    @Test
    public void testA() throws Exception {
        long time = calcRunTime(() -> {
            numbers.forEach((e) -> pool.push(() -> lfll.add(e)));
            pool.join();
        });
    }

    //insert testSize numbers with n threads, then insert testSize numbers more with searching numbers
    //the operation ratios(insertion:search) are 1:1, 1:4, 1:9
    @Test
    public void testB() throws Exception {

        long insertTime = calcRunTime(() -> {
            numbers.forEach((e) -> pool.push(() -> lfll.add(e)));
            pool.join();
        });

        long[] result = new long[this.searchRatio.length];
        for (int i = 0; i < searchRatio.length; ++i) {
            pool = new Pool(numThreads);
            final int ratio = searchRatio[i];
            result[i] = calcRunTime(() -> {
                numbers.forEach((e) -> {
                    if (assertRatio(1, ratio, e)) pool.push(() -> lfll.add(e));
                    else pool.push(() -> lfll.search(e));
                });
                pool.join();
            });
            System.out.println(result[i]);
        }
    }
}
