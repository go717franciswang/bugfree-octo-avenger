public class Percolation {
    private int N;
    private boolean[][] siteOpen;
    private WeightedQuickUnionUF uf;
    private int topRoot = -1;
    private int bottomRoot = -1;
    
    public Percolation(int N) {
        this.N = N;
        siteOpen = new boolean[N][N];
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                siteOpen[i][j] = false;
            }
        }
        
        uf = new WeightedQuickUnionUF(N*N);
    }
    
    public void open(int i, int j) {
        validatePosition(i, j);
        
        siteOpen[i-1][j-1] = true;
        
        if (i == 1) {
            if (topRoot == -1) {
                topRoot = getUFid(i, j);
            } else {
                uf.union(topRoot, getUFid(i, j));
            }
        }
        
        if (i == N) {
            if (bottomRoot == -1) {
                bottomRoot = getUFid(i, j);
            } else {
                uf.union(bottomRoot, getUFid(i, j));
            }
        }
        
        if (i > 1 && isOpen(i-1, j)) {
            uf.union(getUFid(i-1, j), getUFid(i, j));
        }
        
        if (i < N && isOpen(i+1, j)) {
            uf.union(getUFid(i+1, j), getUFid(i, j));
        }
        
        if (j > 1 && isOpen(i, j-1)) {
            uf.union(getUFid(i, j-1), getUFid(i, j));
        }
        
        if (j < N && isOpen(i, j+1)) {
            uf.union(getUFid(i, j+1), getUFid(i, j));
        }
    }
    
    private int getUFid(int i, int j) {
        return (i-1)*N+j-1;
    }
    
    public boolean isOpen(int i, int j) {
        validatePosition(i, j);
        return siteOpen[i-1][j-1];
    }
    
    public boolean isFull(int i, int j) {
        validatePosition(i, j);
        
        if (topRoot == -1) {
            return false;
        }
        
        return isOpen(i, j) && uf.connected(topRoot, getUFid(i, j));
    }
    
    public boolean percolates() {
        if (topRoot == -1 || bottomRoot == -1) {
            return false;
        }
        
        return uf.connected(topRoot, bottomRoot);
    }
    
    private void validatePosition(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }
}