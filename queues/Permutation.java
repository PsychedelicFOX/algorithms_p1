import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

/**
 * @author psy
 * @date 5/12/19.
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int nStrings = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while(!StdIn.isEmpty()){
            nStrings ++;
            String line = StdIn.readString();

            // some magic https://en.wikipedia.org/wiki/Reservoir_sampling
            if (nStrings > k && StdRandom.uniform(nStrings) < k) {
                rq.dequeue();
            } else {
                if (nStrings > k) {continue;}
            }

            rq.enqueue(line);


        }

        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }


    }
}
