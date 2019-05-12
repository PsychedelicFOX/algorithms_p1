/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double COEFF = 1.96;
    private final double cachedConfidenceLo;
    private final double cachedConfidenceHi;
    private final double cachedMean;
    private final double cachedStddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n<=0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] percolationThresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolationThresholds[i] = performTrial(n);
        }

        cachedMean = StdStats.mean(percolationThresholds);
        cachedStddev = StdStats.stddev(percolationThresholds);
        cachedConfidenceLo = (cachedMean - COEFF *cachedStddev/Math.sqrt(trials));
        cachedConfidenceHi = (cachedMean + COEFF *cachedStddev/Math.sqrt(trials));
    }

    private double performTrial(int n) {
        Percolation percolation  = new Percolation(n);
        while (!percolation.percolates()){
            int col = StdRandom.uniform(1, n+1);
            int row = StdRandom.uniform(1, n+1);

            if(percolation.isOpen(row, col)){
                continue;
            }

            percolation.open(row, col);
        }
        return  percolation.numberOfOpenSites() / (1.0 * n * n) ;
    }


    // sample mean of percolation threshold
    public double mean() {
        return cachedMean;
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        return cachedStddev;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return cachedConfidenceLo;
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return cachedConfidenceHi;
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, T);

        System.out.println(String.format("mean                    = %s",percolationStats.mean()));
        System.out.println(String.format("stddev                  = %s",percolationStats.stddev()));
        System.out.println(String.format("95%% confidence interval = [%s, %s]",
                                         percolationStats.confidenceLo(),
                                         percolationStats.confidenceHi()));
    }
}
