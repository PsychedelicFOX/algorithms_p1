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
    private final boolean[] stateGrid;
    private int connected = 0 ;

    // create n-by-n stateGrid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0 ) {
            throw new IllegalArgumentException();
        }
        N = n;
        BOTTOM = n * n  + 1;

        this.weightedQuickUnion = new WeightedQuickUnionUF(n*n+2);
        this.stateGrid = new boolean[N * N];

    }


    private void validate(int row, int col) {
        if (row > N || col > N || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col){
        validate(row, col);
        if (!isOpen(row, col)) {
            connected ++ ;
            stateGrid[getIndex(row, col) - 1] = true;
            checkNeighbor(row, col);
            checkTopAndBottom(row, col);
        }
    }

    private void checkTopAndBottom(int row, int col) {
        if (row == 1) {
            weightedQuickUnion.union(TOP, getIndex(row, col));
        }

        if (row == N){
            weightedQuickUnion.union(BOTTOM, getIndex(row, col));
        }
    }

    private void checkNeighbor(int row, int col) {
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
    }


    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return stateGrid[getIndex(row, col) - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
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

    }
}
