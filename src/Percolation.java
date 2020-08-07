import edu.princeton.cs.algs4.UF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // N-by-N grid
    private int N;
    private int TopSiteIndex;
    private int BottomSiteIndex;
    // number of open sites
    private int numOpen;
    private int [] id;
    // 0 represents closed, 1 represents opened, 2 represents full;
    private int [] state;
    private WeightedQuickUnionUF UnionFind;

    public Percolation(int n) {
        N = n;
        numOpen = 0;
        state = new int[n*n+2];
        UnionFind = new WeightedQuickUnionUF(n*n+2);

        for (int i = 0; i < state.length; i++)
            state[i] = 0;

        // open virtual sites;
        state[0] = 1;
        state[N*N+1] = 1;
        TopSiteIndex = 0;
        BottomSiteIndex = state.length-1;
    }

    /**
     * Return the index for the state[] array
     *
     * ! haven't considered the connor cases
     * @param
     * @throws
     */
    private int toIndex(int row, int col) {
        // considered virtual sites
        return ((col-1)*N+row);
    }

    /**
     * connect p and q in UF
     */
    private boolean connected(int p, int q) {
        return UnionFind.find(p) == UnionFind.find(q);
    }

    private void connectUP(int row, int col) {
        // if at first row, connect to TopSite;
        int Index = toIndex(row,col);
        if (col == 1) {
        //if (row ==1) {
            UnionFind.union(TopSiteIndex,Index);
        } else {
            if (isOpen(row, col-1))
                UnionFind.union(Index,toIndex(row,col-1));
        }
    }

    private void connectDown(int row, int col) {
        // if at bottom row, connect to BottomSite
        int Index = toIndex(row,col);
        if (col == N) {
            UnionFind.union(BottomSiteIndex, Index);
        } else {
            if (isOpen(row,col+1)) {
                UnionFind.union(Index,toIndex(row,col+1));
            }
        }
    }

    private void connectLeft(int row, int col) {
        // attention to the most left border
        int Index = toIndex(row,col);
        if (row == 1)
            return;
        if (isOpen(row-1,col)) {
            UnionFind.union(row,toIndex(row-1,col));
        }
    }

    private  void connectRight(int row, int col) {
        // attention to the most right boarder
        int Index = toIndex(row,col);
        if (row == N)
            return;
        if (isOpen(row+1,col)) {
            UnionFind.union(row,toIndex(row-1,col));
        }
    }

    public void open(int row, int col) {
        connectUP(row,col);
        connectDown(row,col);
        connectLeft(row,col);
        connectRight(row,col);

        state[toIndex(row,col)] = 1;
        numOpen += 1;
    }

    public boolean isOpen(int row, int col) {
        return state[toIndex(row,col)] == 1;
    }

    public boolean isFull(int row, int col) {
        return connected(TopSiteIndex,toIndex(row,col));
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        return connected(TopSiteIndex,BottomSiteIndex);
    }
}