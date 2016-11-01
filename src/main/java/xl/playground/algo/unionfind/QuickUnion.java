package xl.playground.algo.unionfind;

import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * Created by xl on 10/25/16.
 */
public class QuickUnion implements UnionFind {

    private int[] ids;

    public QuickUnion(int numOfComponents) {
        ids = IntStream.range(0, numOfComponents).toArray();
    }

    @Override
    public boolean connected(int p, int q) {
        return rootOf(p) == rootOf(q);
    }

    @Override
    public int count() {
        HashSet<Integer> connectedComponents = new HashSet<>(ids.length);
        for (int i = 0; i < ids.length; i++) {
            connectedComponents.add(rootOf(i));
        }
        return connectedComponents.size();
    }

    @Override
    public void union(int p, int q) {
        int pr = rootOf(p);
        int qr = rootOf(q);
        ids[pr] = qr;
    }

    private int rootOf(int i) {
        int n = ids[i];
        int p = 0;

        while ((p = ids[n]) != n) {
            n = p;
        }

        return n;
    }
}
