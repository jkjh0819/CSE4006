package bst;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import thread.Pool;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//test for report of read write lock binary search tree
@RunWith(Parameterized.class)
public class RWBSTTestReport {
    private int numThreads;
    private int[] searchRatio;
    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    private RWBST tree;
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
        tree = new RWBST();
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
    public RWBSTTestReport(int numThreads) {
        this.numThreads = numThreads;
        //this is for test B
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

    //insert testSize numbers with n threads, then insert testSize numbers more with searching numbers
    //the operation ratios(insertion:search) are 1:1, 1:4, 1:9
    @Test
    public void testB() throws Exception {
        long insertTime = calcRunTime(() -> {
            numbers.forEach((e) -> pool.push(() -> tree.insert(e)));
            pool.join();
        });

        long[] result = new long[this.searchRatio.length];
        for (int i = 0; i < searchRatio.length; ++i) {
            pool = new Pool(numThreads);
            final int ratio = searchRatio[i];
            result[i] = calcRunTime(() -> {
                numbers.forEach((e) -> {
                    if (assertRatio(1, ratio, e)) pool.push(() -> tree.insert(e + testSize));
                    else pool.push(() -> tree.search(e));
                });
                pool.join();
            });
            System.out.println(result[i]);
        }
    }
}
