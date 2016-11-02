package xl.playground.algo.sort;

import java.util.List;

import static xl.playground.algo.sort.Util.exchange;
import static xl.playground.algo.sort.Util.validate;

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
        int l = list.size() / 3;

        while (h < l) {
            h = 3 * h + 1;
        }

        return h;
    }

    public static void main(String... args) {
        validate(Shell::sort);
    }
}
