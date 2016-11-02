package xl.playground.algo.unionfind;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

import static xl.playground.algo.unionfind.Util.validate;

/**
 * Created by xl on 10/25/16.
 */
public class WeightedQuickFind implements UnionFind {

    private int[] ids;
    private int[] weights;

    public WeightedQuickFind(int numOfComponents) {
        ids = IntStream.range(0, numOfComponents).toArray();
        weights = new int[numOfComponents];
        Arrays.fill(weights, 1);
    }

    @Override
    public boolean connected(int p, int q) {
        return rootOf(p) == rootOf(q);
    }

    @Override
    public int count() {
        HashSet<Integer> roots = new HashSet<>();
        for (int i = 0; i < ids.length; i++) {
            roots.add(rootOf(i));
        }
        return roots.size();
    }

    @Override
    public void union(int p, int q) {
        int pr = rootOf(p);
        int qr = rootOf(q);

        if (weights[pr] > weights[qr]) {
            ids[qr] = pr;
            weights[pr] += weights[qr];
        }
        else {
            ids[pr] = qr;
            weights[qr] += weights[pr];
        }
    }

    private int rootOf(int i) {
        int p = 0;

        while ((p = ids[i]) != i) {
            ids[i] = ids[ids[i]];
            i = p;
        }

        return i;
    }

    public static void main(String... args) {
        validate(WeightedQuickFind::new);
    }
}
