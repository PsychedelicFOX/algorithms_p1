import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author psy
 * @date 5/23/19.
 */
public class Board {

    private final int[][] GOAL;
    private final int n;
    private final int[][] grid;


    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks){
        this.n = blocks[0].length;
        this.grid = new int[n][n];
        this.GOAL = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.grid[i][j] = blocks[i][j];
                this.GOAL[i][j] = i * n +j +1;
            }
        }
        GOAL[n-1][n-1] = 0;
    }

    // board dimension n
    public int dimension(){
        return n;
    }
    // number of blocks out of place
    public int hamming(){
        int count = 0;

        for (int i = 0; i < GOAL.length; i++) {
            for (int j = 0; j < GOAL[i].length; j ++){
                if (GOAL[i][j] == 0){
                    continue;
                }
                if (GOAL[i][j] != grid[i][j]){
                    count ++;
                }
            }
        }

        return count;
    }

    private int getRow(int x){
        if (x < n) {
            return 0;
        }
       return  (x - 1) / n;
    }

    private int getCol(int x){
        if (x < n) {
            return x - 1;
        }
       return  (x - 1) % n;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan(){
        int sum = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j ++){
                if (grid[i][j] == 0){
                    continue;
                }
                int val = grid[i][j];
                int expectedI = getRow(val);
                int expectedJ = getCol(val);
                int diff = Math.abs(i - expectedI) + Math.abs(j - expectedJ);

                sum += diff;


            }
        }

        return sum;
    }
    // is this board the goal board?
    public boolean isGoal(){

        return Arrays.deepEquals(this.grid, GOAL);
    }

    private int[][] getGOAL(){
        int [][]GOAL = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.GOAL[i][j] = i * n +j +1;
            }
        }
        GOAL[n-1][n-1] = 0;
        return GOAL;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin(){
        int[][] newGrid = getGridCopy();


        if (grid[0][0] != 0 && grid[0][1] != 0) {
            swap(newGrid, 0, 0, 0,1 );
        } else {
            swap(newGrid, n-1, n-1, n-1, n-2);
        }

        return new Board(newGrid);


    }

    private int[][] getGridCopy() {
        int[][]newGrid = new int[n][n];

        for (int i = 0; i < grid.length; i++) {
            newGrid[i] = Arrays.copyOf(grid[i], n);
        }
        return newGrid;
    }

    private int[][] swap(int[][] newGrid, int iA, int jA, int iB, int jB) {
        int tmp = newGrid[iA][jA];
        newGrid[iA][jA] = newGrid[iB][jB] ;
        newGrid[iB][jB] = tmp;
        return newGrid;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if (y instanceof Board) {
            return Arrays.deepEquals(this.grid, ((Board) y).grid);
        }
        return false;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> boardList = new ArrayList<>();


        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++){
                if (grid[i][j] == 0) {
                    if (i> 0 ) {
                        int[][] tmp = getGridCopy();
                        boardList.add(new Board(swap(tmp, i, j, i-1, j)));
                    }

                    if (i + 1 < n) {
                        int[][] tmp = getGridCopy();
                        boardList.add(new Board(swap(tmp, i, j, i+1, j)));
                    }

                    if (j > 0){
                        int[][] tmp = getGridCopy();
                        boardList.add(new Board(swap(tmp, i, j, i, j-1)));
                    }

                    if (j + 1 < n){
                        int[][] tmp = getGridCopy();
                        boardList.add(new Board(swap(tmp, i, j, i, j+1)));
                    }
                    break;
                }
            }
        }

        return boardList;
    }
    // string representation of this board (in the output format specified below)
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    private static void test1() {
        Board  b1 = new Board(new int[][]{{1,3, 8},{2,0, 7}, {4, 5 ,6}});
        assert b1.hamming() == 7;
        assert !b1.isGoal();
    }

    private static void test2() {
        Board n1 = new Board(new int[][]{{1,2,3},{4, 5, 6}, {7 ,8, 0}});
        assert n1.hamming() == 0;
        assert n1.isGoal();
    }

    private static void test3() {
        Board n1 = new Board(new int[][]{{8, 1, 3},{4, 0, 2}, {7 ,6, 5}});
        assert n1.hamming() == 5;
        assert n1.manhattan() == 10;
    }


    private static void test4() {
        int[][] data = {{2, 0, 3, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}};
        Board b = new Board(data);
        assert b.neighbors().spliterator().estimateSize() == 3;
    }

    private static void test5() {
        int[][] data = {{2, 0, 3, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}};
        int[][] dataTwin = {{2, 0, 3, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 15, 11}};
        Board b = new Board(data);
        Board twin = new Board(dataTwin);


        assert b.twin().equals(twin);
    }

    private static void test6() {
        int[][] data = {{2, 3, 0, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}};
        int[][] dataTwin = {{3, 2, 0, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}};
        Board b = new Board(data);
        Board twin = new Board(dataTwin);
        assert b.twin().equals(twin);
    }
}
