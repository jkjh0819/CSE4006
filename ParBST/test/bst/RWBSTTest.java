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

public class RWBSTTest {

    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    private static RWBST tree;
    private static Pool pool;

    @BeforeClass
    public static void init() throws Exception {
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
    }

    @Before
    public void prepareTest() throws Exception {
        tree = new RWBST();
        pool = new Pool(4);
    }

    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        numbers.forEach((e) -> assertTrue(tree.search(e)));
    }

    @Test
    public void insertParallel() throws Exception {
        numbers.forEach((e) -> pool.push(() -> tree.insert(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(tree.search(e)));
    }

    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.search(e));
    }

    @Test
    public void searchParallel() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(tree.search(e))));
        pool.join();
    }
}