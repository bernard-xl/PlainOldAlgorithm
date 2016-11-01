package xl.playground.algo.unionfind;

/**
 * Created by xl on 10/25/16.
 */
public interface UnionFind {

    boolean connected(int p, int q);

    int count();

    void union(int p, int q);
}
