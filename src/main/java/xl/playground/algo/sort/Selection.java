package xl.playground.algo.sort;

import java.util.List;

import static xl.playground.algo.sort.Util.exchange;
import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/1/16.
 */
public class Selection {

    public static <T extends Comparable<T>> void sort(List<T> list) {

        for (int i = 0; i < list.size(); i++) {
            int min = findMinimumIndex(list, i);
            exchange(list, i, min);
        }
    }

    private static <T extends Comparable<T>> int findMinimumIndex(List<T> list, int from) {
        int minimumIndex = from;
        T minimumValue = list.get(from);

        for (int i = from + 1; i < list.size(); i++) {
            T currentValue = list.get(i);

            if (minimumValue.compareTo(currentValue) > 0) {
                minimumIndex = i;
                minimumValue = currentValue;
            }
        }

        return minimumIndex;
    }

    public static void main(String... args) {
        validate(Selection::sort);
    }
}
