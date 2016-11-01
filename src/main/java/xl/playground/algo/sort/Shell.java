package xl.playground.algo.sort;

import java.util.List;

import static xl.playground.algo.sort.Util.exchange;
import static xl.playground.algo.sort.Util.validateSortingAlgorithm;

/**
 * Created by xl on 11/1/16.
 */
public class Shell {

    public static <T extends Comparable<T>> void sort(List<T> list) {
        int h = findH(list);

        while (h >= 1) {
            for(int i = h; i < list.size(); i++) {
                T currentValue = list.get(i);
                for (int j = i; j >= h && list.get(j - h).compareTo(currentValue) > 0; j -= h) {
                    exchange(list, j, j - h);
                }
            }
            h /= 3;
        }
    }

    private static <T extends Comparable<T>> int findH(List<T> list) {
        int h = 1;
        int i = 1;
        int trial = 1;
        while((trial = 3 * i + 1) < list.size()) {
            h = trial;
            i++;
        }
        return h;
    }

    public static void main(String... args) {
        validateSortingAlgorithm(Shell::sort);
    }
}
