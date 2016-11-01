package xl.playground.algo.unionfind.exercise;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by xl on 10/25/16.
 */
public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[][] grid;
    private int virtualTop;
    private int virtualBottom;
    private int width;

    public Percolation(int n) {
        uf = new WeightedQuickUnionUF(n * n + 2);
        grid = new boolean[n][n];
        width = n;
        virtualTop = n * n;
        virtualBottom = n * n + 1;
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }

        int currentId = idOf(row, col);

        grid[row][col] = true;
        if (row == 0) uf.union(currentId, virtualTop);
        if (row == width - 1) uf.union(currentId, virtualBottom);

        if (row > 0 && isOpen(row - 1, col)) uf.union(currentId, idOf(row - 1, col));
        if (col > 0 && isOpen(row, col - 1)) uf.union(currentId, idOf(row, col - 1));
        if (row < width - 1 && isOpen(row + 1, col)) uf.union(currentId, idOf(row + 1, col));
        if (col < width - 1 && isOpen(row, col + 1)) uf.union(currentId, idOf(row, col + 1));
    }

    public boolean isOpen(int row, int col) {
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        return !grid[row][col];
    }

    public boolean percolates() {
        return uf.connected(virtualTop, virtualBottom);
    }

    private int idOf(int row, int col) {
        return (row * width) + col;
    }

    public static void main(String...args) {
        Percolation p = new Percolation(4);
        p.open(0, 0);
        p.open(1, 0);
        p.open(1, 1);
        p.open(2, 1);
        p.open(2, 2);
        p.open(3, 2);
        System.out.println("Is percolated? " + p.percolates());
    }
}
