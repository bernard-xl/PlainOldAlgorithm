package xl.playground.algo.unionfind;

import java.util.HashSet;
import java.util.stream.IntStream;

import static xl.playground.algo.unionfind.Util.validate;

/**
 * Created by xl on 10/25/16.
 */
public class QuickFind implements UnionFind {

    private int[] ids;

    public QuickFind(int numOfComponents) {
        ids = IntStream.range(0, numOfComponents).toArray();
    }

    @Override
    public boolean connected(int p, int q) {
        return ids[p] == ids[q];
    }

    @Override
    public int count() {
        HashSet<Integer> connectedComponents = new HashSet<>(ids.length);
        for (int i : ids) {
            connectedComponents.add(i);
        }
        return connectedComponents.size();
    }

    @Override
    public void union(int p, int q) {
        int pid = ids[p];
        int qid = ids[q];

        if (pid != qid) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] == pid) {
                    ids[i] = qid;
                }
            }
        }
    }

    public static void main(String... args) {
        validate(QuickFind::new);
    }
}
