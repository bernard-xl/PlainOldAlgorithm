package xl.playground.algo.unionfind;

/**
 * Created by xl on 10/25/16.
 */
public class UnionFindTest {

    public static void main(String... args) {
        UnionFind uf = new WeightedQuickFind(8);
        uf.union(0, 2);
        uf.union(1, 2);
        uf.union(4, 5);
        uf.union(6, 7);
        // uf.union(5, 6);
        System.out.println("0 and 2 connected? " + uf.connected(0, 2));
        System.out.println("0 and 4 connected? " + uf.connected(0, 4));
        System.out.println("4 and 5 connected? " + uf.connected(4, 5));
        System.out.println("4 and 7 connected? " + uf.connected(4, 7));
        System.out.println("There are " + uf.count() + " connected groups.");
    }
}
