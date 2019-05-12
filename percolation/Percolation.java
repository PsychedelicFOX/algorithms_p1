/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int N;
    private final int BOTTOM ;
    private final int TOP = 0 ;
    private final WeightedQuickUnionUF weightedQuickUnion;
    private final boolean[] grid;
    private int connected = 0 ;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0 ) {
            throw new IllegalArgumentException();
        }
        N = n;
        BOTTOM = n * n  + 1;

        this.weightedQuickUnion = new WeightedQuickUnionUF(n*n+2);
        this.grid = new boolean[N * N];

    }


    private void    checkRowCol(int row, int col) {
        if (row > N || col > N || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col){
        checkRowCol(row, col);
        if (!isOpen(row, col)) {
            connected ++ ;
            grid[getIndex(row, col) - 1] = true;

            // left
            if(col != 1){
                if (isOpen(row, col - 1)){
                    weightedQuickUnion.union(getIndex(row, col),
                                             getIndex(row, col-1));
                }
            }

            // right
            if(col != N){
                if (isOpen(row, col + 1)){
                    weightedQuickUnion.union(getIndex(row, col),
                                             getIndex(row, col+ 1));
                }
            }

            // top
            if(row != 1){
                if (isOpen(row-1, col )){
                    weightedQuickUnion.union(getIndex(row, col),
                                             getIndex(row-1, col));
                }
            }

            // bottom
            if(row != N){
                if (isOpen(row+1, col )){
                    weightedQuickUnion.union(getIndex(row, col),
                                             getIndex(row+1, col));
                }
            }


            if (row == 1) {
                weightedQuickUnion.union(TOP, getIndex(row, col));
            }

            if (row == N){
                weightedQuickUnion.union(BOTTOM, getIndex(row, col));
            }

        }
    }



    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return grid[getIndex(row, col) - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        return weightedQuickUnion.connected(TOP, getIndex(row, col));
    }
    // number of open sites
    public int numberOfOpenSites()      {
        return connected;
    }
    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnion.connected(TOP, BOTTOM);
    }

    private int getIndex(int row, int col) {
        return N * (row-1) + col;
    }

    public static void main(String[] args)  {
        Percolation p = new Percolation(10);
        p.open(0,5);

    } // test client (optional)
}
