package xl.playground.algo.sort;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.LongStream;

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

    public static void validateSortingAlgorithm(Consumer<List<Long>> algorithm) {
        long seed = System.nanoTime();

        for (int i = 1; i <= 5; i++) {
            int dataSize = (int)Math.pow(10, i);

            List<Long> data = generateRandomNumbers(seed, dataSize);

            long beginTime = System.nanoTime();
            algorithm.accept(data);
            long endTime = System.nanoTime();

            assert isSorted(data);

            System.out.printf("Time elapsed for sorting %d elements: %.2f ms\n", dataSize, (endTime - beginTime) / 1000000.0);
        }
    }

    private static List<Long> generateRandomNumbers(long seed, int n) {
        return LongStream.iterate(seed, i -> (i * 59 + 3333) % 175795)
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
