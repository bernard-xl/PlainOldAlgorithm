package xl.playground.algo.sort;

import java.util.ArrayList;
import java.util.List;

import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/2/16.
 */
public class MergeBottomUp {

    public static <T extends Comparable<T>> void sort(List<T> list) {
        List<T> src = list;
        List<T> dest = new ArrayList<>(list);
        int n = list.size();

        for (int len = 1; len < n; len *= 2) {
            for(int lo = 0; lo < n - len; lo += len + len) {
                int mi = lo + len;
                int hi = Math.min(lo + len + len, n);

                merge(src, dest, lo, mi, hi);
            }
            List<T> tmp = src;
            src = dest;
            dest = tmp;
        }
    }

    private static <T extends Comparable<T>> void merge(List<T> src, List<T> dest, int lo, int mi, int hi) {
        int i = lo;
        int j = mi;

        for(int k = lo; k < hi; k++) {
            if (i >= mi) {
                dest.set(k, src.get(j++));
            }
            else if (j >= hi) {
                dest.set(k, src.get(i++));
            }
            else if (src.get(i).compareTo(src.get(j)) > 0) {
                dest.set(k, src.get(j++));
            }
            else {
                dest.set(k, src.get(i++));
            }
        }
    }

    public static void main(String... args) {
        validate(MergeBottomUp::sort);
    }
}
