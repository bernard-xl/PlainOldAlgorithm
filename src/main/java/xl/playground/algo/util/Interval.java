package xl.playground.algo.util;

import java.util.Objects;

/**
 * Created by xl on 12/15/16.
 */
public class Interval<E extends Comparable<E>> implements Comparable<Interval<E>> {

    private E start;
    private E end;

    public Interval(E start, E end) {
        this.start = start;
        this.end = end;
    }

    public E getStart() {
        return start;
    }

    public E getEnd() {
        return end;
    }

    @Override
    public int compareTo(Interval<E> that) {
        return this.start.compareTo(that.start);
    }

    public boolean isIntersectWith(Interval<E> that) {
        boolean cond1 = that.start.compareTo(this.end) < 0;
        boolean cond2 = this.start.compareTo(that.end) < 0;

        return cond1 && cond2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return Objects.equals(start, interval.start) &&
                Objects.equals(end, interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}
