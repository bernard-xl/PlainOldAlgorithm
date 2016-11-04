package xl.playground.algo.sort;

import java.util.Arrays;
import java.util.List;

import static xl.playground.algo.sort.Util.exchange;
import static xl.playground.algo.sort.Util.validate;

/**
 * Created by xl on 11/3/16.
 */
public class Quick {

    private static final int PIVOT_CUTOFF = 36;

    public static <T extends Comparable<T>> T select(List<T> list, int k) {
        if (k < 0 || k >= list.size()) {
            throw new IndexOutOfBoundsException("size of list is " + list.size() + ", but selected rank is " + k);
        }
        select(list, 0, list.size() - 1, k);
        return list.get(k);
    }

    public static <T extends Comparable<T>> void select(List<T> list, int lo, int hi, int k) {
        if (lo >= hi) {
            return;
        }

        int n = partition(list, lo, hi);

        if (n < k) {
            select(list, n + 1, hi, k);
        }
        else if (n > k) {
            select(list, lo, n - 1, k);
        }
    }

    public static <T extends Comparable<T>> void sort(List<T> list) {
        sort(list, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> void sort(List<T> list, int lo, int hi) {
        if (lo >= hi) {
            return;
        }

        PartitioningResult p = dijkstraPartition(list, lo, hi);
        sort(list, lo, p.lessThanPivot);
        sort(list, p.greaterThanPivot, hi);
    }

    private static <T extends Comparable<T>> int partition(List<T> list, int lo, int hi) {
        int pi = pickPivot(list, lo, hi);
        int i = lo;
        int j = hi + 1;

        T pv = list.get(pi);
        exchange(list, lo, pi);

        while (true) {
            while (list.get(++i).compareTo(pv) <= 0) {
                if (i == hi) break;
            }
            while (list.get(--j).compareTo(pv) > 0) {
                if (j == lo) break;
            }

            if (i >= j) break;

            exchange(list, i, j);
        }

        exchange(list, lo, j);
        return j;
    }

    // Dijkstra 3-way partitioning
    private static <T extends Comparable<T>> PartitioningResult dijkstraPartition(List<T> list, int lo, int hi) {
        int lt = lo;
        int gt = hi;
        int i = lo;

        int pa = pickPivot(list, lo, hi);
        T pv = list.get(pa);

        while (i <= gt) {
            int co = list.get(i).compareTo(pv);

            if (co < 0) {
                exchange(list, lt++, i++);
            }
            else if (co > 0) {
                exchange(list, gt--, i);
            }
            else {
                i++;
            }
        }
        return new PartitioningResult(lt - 1, gt + 1);
    }

    private static <T extends Comparable<T>> int pickPivot(List<T> list, int lo, int hi) {
        int n = hi - lo + 1;
        int mi = lo + n / 2;
        if (n < PIVOT_CUTOFF) {
            return medianOfThree(list, lo, mi, hi);
        } else {
            // Tukey's ninther
            int in = n / 8;
            int a = medianOfThree(list, lo, lo + in, lo + in + in);
            int b = medianOfThree(list, mi - in, mi, mi + in);
            int c = medianOfThree(list, hi - in - in, hi - in, hi);
            return medianOfThree(list, a, b, c);
        }
    }

    private static <T extends Comparable<T>> int medianOfThree(List<T> list, int a, int b, int c) {
        T av = list.get(a);
        T bv = list.get(b);
        T cv = list.get(c);

        if (av.compareTo(bv) < 0) {
            if (cv.compareTo(av) <= 0) return a;
            if (cv.compareTo(bv) <= 0) return c;
            return b;
        }
        else {
            if (cv.compareTo(bv) <= 0) return b;
            if (cv.compareTo(av) <= 0) return c;
            return a;
        }
    }

    private static class PartitioningResult {
        public int lessThanPivot;
        public int greaterThanPivot;

        public PartitioningResult(int lessThanPivot, int greaterThanPivot) {
            this.lessThanPivot = lessThanPivot;
            this.greaterThanPivot = greaterThanPivot;
        }
    }

    public static void main(String... args) {
        validate(Quick::sort);

        // quick select validation
        List<Integer> trials = Arrays.asList(0, 3, 6, 9);
        for (int i : trials) {
            List<Integer> list = Arrays.asList(5, 4, 3, 2, 1, 9, 8, 7, 6, 0);
            Integer selected = select(list, i);
            if (selected == i) {
                System.out.println("Selected " + selected + ", expect " + i);
            } else {
                throw new IllegalStateException("Selected wrong number, expect " + i + " but " + selected + " is selected.");
            }
        }
    }
}
