package xl.playground.algo.sort;

import java.util.ArrayList;
import java.util.List;

import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/2/16.
 */
public class MergeBottomUp {

    public static <T extends Comparable<T>> void sort(List<T> list) {
        int n = list.size();
        List<T> aux = new ArrayList<>(list);

        for (int len = 1; len < n; len *= 2) {
            for(int lo = 0; lo < n - len; lo += len + len) {
                int mi = lo + len;
                int hi = Math.min(lo + len + len, n);

                merge(list, aux, lo, mi, hi);
            }
        }
    }

    private static <T extends Comparable<T>> void merge(List<T> list, List<T> aux, int lo, int mi, int hi) {
        int i = lo;
        int j = mi;

        for(int k = lo; k < hi; k++) {
            if (i >= mi) {
                aux.set(k, list.get(j++));
            }
            else if (j >= hi) {
                aux.set(k, list.get(i++));
            }
            else if (list.get(i).compareTo(list.get(j)) > 0) {
                aux.set(k, list.get(j++));
            }
            else {
                aux.set(k, list.get(i++));
            }
        }

        for (int k = lo; k < hi; k++) {
            list.set(k, aux.get(k));
        }
    }

    public static void main(String... args) {
        validate(MergeBottomUp::sort);
    }
}
