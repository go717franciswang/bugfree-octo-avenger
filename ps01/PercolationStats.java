public class PercolationStats {
    private int N;
    private int T;
    private double[] xs;
    
    public PercolationStats(int N, int T) {       
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        
        this.N = N;
        this.T = T;
        xs = new double[T];
        
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            int opened = 0;
            
            while (!p.percolates()) {
                int[] closedSite = getRandomClosedSite(p);
                p.open(closedSite[0], closedSite[1]);
                opened++;
            }
            
            xs[i] = (double) opened / (N*N);
        }
    }
    
    private int[] getRandomClosedSite(Percolation percolation) {
        while (true) {
            int i = StdRandom.uniform(1, N+1);
            int j = StdRandom.uniform(1, N+1);
            
            if (!percolation.isOpen(i, j)) {
                return new int[]{i, j};
            }
        }
    }
    
    public double mean() {
        return StdStats.mean(xs);
    }
    
    public double stddev() {
        return StdStats.stddev(xs);
    }
    
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }
    
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
    
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats ps = new PercolationStats(N, T);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo()
                          + ", " + ps.confidenceHi());
    }
}