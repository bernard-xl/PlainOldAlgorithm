package xl.playground.algo.sort;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static xl.playground.algo.sort.Util.exchange;

/**
 * Created by xl on 11/1/16.
 */
public class Knuth {

    public static <T> void shuffle(List<T> list) {
        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            int rnd = random.nextInt(i + 1);
            exchange(list, i, rnd);
        }
    }

    public static void main(String... args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println("Before: " + list);
        shuffle(list);
        System.out.println("After: " + list);
    }
}
