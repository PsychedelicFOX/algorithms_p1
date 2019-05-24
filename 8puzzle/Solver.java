import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

/**
 * @author psy
 * @date 5/23/19.
 */
public class Solver {
    private MinPQ<Board> pq;
        // find a solution to the initial board (using the A* algorithm)
   public Solver(Board initial){
       pq = new MinPQ<>(new Comparator<Board>() {
           @Override
           public int compare(Board o1, Board o2) {
               return Integer.compare(o1.manhattan(), o2.manhattan());
           }
       });

   }

    // is the initial board solvable?
   public boolean isSolvable(){
        return false;
   }

    // min number of moves to solve initial board; -1 if unsolvable
   public int moves(){
       return -1;
   }

    // sequence of boards in a shortest solution; null if unsolvable
   public Iterable<Board> solution() {
       return null;
   }
   public static void main(String[] args){

   }
}
