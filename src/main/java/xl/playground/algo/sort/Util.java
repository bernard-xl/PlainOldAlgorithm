package xl.playground.algo.sort;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * Created by xl on 11/1/16.
 */
public class Util {

    public static <T> void exchange(List<T> list, int i, int n) {
        T tmp = list.get(i);
        list.set(i, list.get(n));
        list.set(n, tmp);
    }

    public static void validate(Consumer<List<Long>> algorithm) {
        long seed = System.nanoTime();

        for (int i = 1; i <= 5; i++) {
            int dataSize = (int)Math.pow(10, i);

            List<Long> data = generateRandomNumbers(seed, dataSize);

            long beginTime = System.nanoTime();
            algorithm.accept(data);
            long endTime = System.nanoTime();

            if (!isSorted(data)) {
                throw new IllegalStateException("the collection is not sorted");
            }

            System.out.printf("Time elapsed for sorting %d elements: %.2f ms\n", dataSize, (endTime - beginTime) / 1000000.0);
        }
    }

    private static List<Long> generateRandomNumbers(long seed, int n) {
        return new Random(seed)
                .longs(0, 10000)
                .limit(n)
                .boxed()
                .collect(toList());
    }

    private static boolean isSorted(List<Long> list) {
        if (list.isEmpty()) {
            return true;
        }

        Long prevElem = list.get(0);

        for (Long elem : list) {
            if (prevElem > elem) {
                return false;
            }
            prevElem = elem;
        }

        return true;
    }

}
