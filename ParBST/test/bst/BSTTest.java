package bst;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thread.Pool;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BSTTest {

    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    private static BST tree;
    private static Pool pool;

    @BeforeClass
    public static void init() throws Exception {
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
    }

    @Before
    public void prepareTest() throws Exception {
        tree = new BST();
        pool = new Pool(4);
    }

    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        numbers.forEach((e) -> assertTrue(tree.search(e)));
    }

    @Test
    public void delete() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.delete(e));
        numbers.forEach((e) -> assertFalse(tree.search(e)));
    }

    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> tree.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.search(e));
    }

    @Test
    public void testAll() throws Exception {
        Set<Integer> verifier = new HashSet<>();
        Random random = new Random(0);
        numbers.forEach((e) -> {
            tree.insert(e);
            verifier.add(e);
        });
        Collections.shuffle(numbers);
        for (int i = 0; i < testSize; ++i) {
            int finalI = i;
            switch (random.nextInt(3)) {
                case 0:
                    pool.push(() -> tree.insert(numbers.get(finalI)));
                    verifier.add(numbers.get(finalI));
                    break;
                case 1:
                    pool.push(() -> tree.delete(numbers.get(finalI)));
                    verifier.remove(numbers.get(finalI));
                    break;
                case 2:
                    pool.push(() -> tree.search(numbers.get(finalI)));
                    break;
            }
        }
        pool.join();
        numbers.forEach((e) -> assertEquals(verifier.contains(e), tree.search(e)));
    }
}