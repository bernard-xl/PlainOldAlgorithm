package xl.playground.algo.sort;

import java.util.List;

import static xl.playground.algo.sort.Util.exchange;
import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/1/16.
 */
public class Insertion {

    public static <T extends Comparable<T>> void sort(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            T currentValue = list.get(i);
            for (int j = i; j >= 1 && list.get(j - 1).compareTo(currentValue) > 0; j--) {
                exchange(list, j, j - 1);
            }
        }
    }

    public static void main(String... args) {
        validate(Insertion::sort);
    }
}
