package xl.playground.algo.sort;

import java.util.ArrayList;
import java.util.List;

import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/1/16.
 */
public class Merge {

    private static final int CUTOFF = 7;

    public static <T extends Comparable<T>> void sort(List<T> list) {
        List<T> buff = new ArrayList<>(list);
        sort(buff, list, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> void sort(List<T> src, List<T> dest, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;

        sort(dest, src, lo, mid);
        sort(dest, src, mid + 1, hi);

        merge(src, dest, lo, mid, hi);
    }

    private static <T extends Comparable<T>> void merge(List<T> src, List<T> dest, int lo, int mi, int hi) {
        int i = lo;
        int j = mi + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mi) {
                dest.set(k, src.get(j++));
            }
            else if (j > hi) {
                dest.set(k,src.get(i++));
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
        validate(Merge::sort);
    }
}
