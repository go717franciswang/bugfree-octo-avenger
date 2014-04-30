public class Board {
    private int[][] blocks;
    private int hamming;
    private int manhattan;
    
    public Board(int[][] blocks) {
        this.blocks = blocks;
        hamming = -1;
        manhattan = -1;
    }
    
    public int hamming() {
        if (hamming == -1) {
            int d = 0;
            
            for (int i=0; i<blocks.length; i++) {
                for (int j=0; j<blocks.length; j++) {
                    int actual = blocks[i][j];
                    int expected = i*blocks.length + j + 1;
                    if (actual > 0 && actual != expected) {
                        d++;
                    }
                }
            }
            
            hamming = d;
        }
            
        return hamming;
    }
    
    public int manhattan() {
        if (manhattan == -1) {
            int d = 0;
            
            for (int i=0; i<blocks.length; i++) {
                for (int j=0; j<blocks.length; j++) {
                    int actual = blocks[i][j];
                    
                    if (actual > 0) {
                        actual--;
                        int expected_row = actual / blocks.length;
                        int expected_col = actual % blocks.length;
                        d += Math.abs(expected_row - i) + Math.abs(expected_col - j);
                    }
                }
            }
            
            manhattan = d;
        }
        
        return manhattan;
    }
    
    public boolean isGoal() {
        return hamming() == 0;
    }
   
    public Board twin() {
        int[][] twinBlocks;
        
        if (blocks[0][0] == 0 || blocks[0][1] == 0) {
            twinBlocks = exchangedBlocks(1, 0, 1, 1);
        } else {
            twinBlocks = exchangedBlocks(0, 0, 0, 1);
        }
        
        return new Board(twinBlocks);
    }
    
    public int dimension() {
        return blocks.length;
    }
    
    private int[][] exchangedBlocks(int i1, int j1, int i2, int j2) {
        int[][] copiedBlocks = copyOfBlocks();
        int tmp = blocks[i1][j1];
        copiedBlocks[i1][j1] = blocks[i2][j2];
        copiedBlocks[i2][j2] = tmp;
        
        return copiedBlocks;
    }
    
    private int[][] copyOfBlocks() {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int i=0; i<blocks.length; i++) {
            for (int j=0; j<blocks.length; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        
        return copy;
    }
    
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        
        if (y.getClass() != this.getClass()) {
            return false;
        }
        
        Board that = (Board) y;
        if (blocks.length != that.blocks.length) {
            return false;
        }
        
        for (int i=0; i<blocks.length; i++) {
            for (int j=0; j<blocks.length; j++) {
                if (blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int[] empty = findEmpty();
        int i = empty[0];
        int j = empty[1];
        
        if (i > 0) {
            q.enqueue(new Board(exchangedBlocks(i, j, i-1, j)));
        }
        
        if (i < blocks.length-1) {
            q.enqueue(new Board(exchangedBlocks(i, j, i+1, j)));
        }
        
        if (j > 0) {
            q.enqueue(new Board(exchangedBlocks(i, j, i, j-1)));
        }
        
        if (j < blocks.length-1) {
            q.enqueue(new Board(exchangedBlocks(i, j, i, j+1)));
        }
        
        return q;
    }
    
    private int[] findEmpty() {
        for (int i=0; i<blocks.length; i++) {
            for (int j=0; j<blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        
        return new int[]{-1, -1};
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(blocks.length + "\n");
        
        for (int i=0; i<blocks.length; i++) {
            for (int j=0; j<blocks.length; j++) {
                s.append(String.format("%2d", blocks[i][j]) + " ");
            }
            
            s.append("\n");
        }
        
        return s.toString();
    }
}