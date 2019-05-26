import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @author psy
 * @date 5/23/19.
 */
public class Solver {
    private SearchNode solution;
        // find a solution to the initial board (using the A* algorithm)
   public Solver(Board initial){
       if (initial == null) {
           throw  new IllegalArgumentException();
       }

       MinPQ<SearchNode> pq = new MinPQ<>();
       MinPQ<SearchNode> pqTwin = new MinPQ<>();

       pq.insert(new SearchNode(initial, null, -1));
       pqTwin.insert(new SearchNode(initial.twin(), null, -1));


       SearchNode candidate = pq.delMin();
       SearchNode candidateTwin  = pqTwin.delMin();

       while (!candidate.board.isGoal() && !candidateTwin.board.isGoal()) {
           for (Board neighbor : candidate.board.neighbors()) {
               if (candidate.predecessor == null) {
                   pq.insert(new SearchNode(neighbor, candidate, candidate.moves));
               } else if (!neighbor.equals(candidate.predecessor.board)){
                   pq.insert(new SearchNode(neighbor, candidate, candidate.moves));
               }
           }

           for (Board neighbor : candidateTwin.board.neighbors()) {
               if (candidateTwin.predecessor == null) {
                   pqTwin.insert(new SearchNode(neighbor, candidateTwin, candidateTwin.moves));
               } else if (!neighbor.equals(candidateTwin.predecessor.board)){
                   pqTwin.insert(new SearchNode(neighbor, candidateTwin, candidateTwin.moves));
               }
           }

           candidate = pq.delMin();
           candidateTwin = pqTwin.delMin();


       }

       solution = candidate.board.isGoal() ? candidate : null;

   }

   public static void main(String[] args){

       // create initial board from file
       In in = new In(args[0]);
       int n = in.readInt();
       int[][] blocks = new int[n][n];
       for (int i = 0; i < n; i++)
           for (int j = 0; j < n; j++)
               blocks[i][j] = in.readInt();
       Board initial = new Board(blocks);

       // solve the puzzle
       Solver solver = new Solver(initial);

       // print solution to standard output
       if (!solver.isSolvable())
           StdOut.println("No solution possible");
       else {
           StdOut.println("Minimum number of moves = " + solver.moves());
           for (Board board : solver.solution())
               StdOut.println(board);
       }
   }

    // is the initial board solvable?
   public boolean isSolvable(){
        return solution != null;
   }

    // min number of moves to solve initial board; -1 if unsolvable
   public int moves(){
       if (!isSolvable()) {
           return -1;
       }

       return solution.moves;
   }

    // sequence of boards in a shortest solution; null if unsolvable
   public Iterable<Board> solution() {

       if (!isSolvable()) {
           return null;
       }

       Stack<Board> res = new Stack<>();

       for(SearchNode node = solution; node != null; node = node.predecessor){
           res.push(node.board);
       }


       return res;
   }

   private class SearchNode implements Comparable<SearchNode>{
       private final Board board;
       private final SearchNode predecessor;
       private final int moves;
       private final int manhattan;

       public SearchNode(Board board, SearchNode predecessor, int moves){
           this.board = board;
           this.predecessor = predecessor;
           this.moves = moves + 1;
           this.manhattan = board.manhattan();
       }


       @Override
       public int compareTo(SearchNode o) {
           return Integer.compare(this.manhattan + moves, o.manhattan + o.moves);
       }


   }
}
