import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] E;
    
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        buildE();
    }
    
    private void buildE() {
        E = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                E[x][y] = energy(x, y);
            }
        }
    }
    
    public Picture picture() {
        return picture;
    }
    
    public int width() {
        return picture.width();
    }
    
    public int height() {
        return picture.height();
    }
    
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        
        if (x == 0 || y == 0 || x == width()-1 || y == height()-1) {
            return 195075d;
        }
        
        return colorDiffSq(picture.get(x-1, y), picture.get(x+1, y)) +
            colorDiffSq(picture.get(x, y-1), picture.get(x, y+1));
    }
    
    private double colorDiffSq(Color a, Color b) {
        return Math.pow(b.getRed() - a.getRed(), 2) + 
            Math.pow(b.getGreen() - a.getGreen(), 2) + 
            Math.pow(b.getBlue() - a.getBlue(), 2); 
    }
    
    public int[] findHorizontalSeam() {
        double[][] pathTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        
        for (int y = 0; y < height(); y++) {
            pathTo[0][y] = E[0][y];
        }
        
        for (int x = 0; x < width()-1; x++) {
            for (int y = 0; y < height(); y++) {
                for (int i = -1; i <= 1; i++) {
                    if (y+i >= 0 && y+i < height()) {
                        double e = pathTo[x][y] + E[x+1][y+i];
                        
                        if (pathTo[x+1][y+i] == 0d || e < pathTo[x+1][y+i]) {
                            pathTo[x+1][y+i] = e;
                            edgeTo[x+1][y+i] = y;
                        }
                    }
                }
            }
        }
        
        double minPath = -1d;
        int ymin = -1;
        int x = width() - 1;
        for (int y = 0; y < height(); y++) {
            if (y == 0 || minPath > pathTo[x][y]) {
                minPath = pathTo[x][y];
                ymin = y;
            }
        }
        
        int[] a = new int[width()];
        a[width()-1] = ymin;
        for (x = width()-2; x >= 0; x--) {
            a[x] = edgeTo[x+1][a[x+1]];
        }
        
        return a;
    }
    
    public int[] findVerticalSeam() {
        double[][] pathTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        
        for (int x = 0; x < width(); x++) {
            pathTo[x][0] = E[x][0];
        }
        
        for (int y = 0; y < height()-1; y++) {
            for (int x = 0; x < width(); x++) {
                for (int i = -1; i <= 1; i++) {
                    if (x+i >= 0 && x+i < width()) {
                        double e = pathTo[x][y] + E[x+i][y+1];
                        
                        if (pathTo[x+i][y+1] == 0d || e < pathTo[x+i][y+1]) {
                            pathTo[x+i][y+1] = e;
                            edgeTo[x+i][y+1] = x;
                        }
                    }
                }
            }
        }
        
        double minPath = -1d;
        int xmin = -1;
        int y = height() - 1;
        for (int x = 0; x < width(); x++) {
            if (x == 0 || minPath > pathTo[x][y]) {
                minPath = pathTo[x][y];
                xmin = x;
            }
        }
        
        int[] a = new int[height()];
        a[height()-1] = xmin;
        for (y = height()-2; y >= 0; y--) {
            a[y] = edgeTo[a[y+1]][y+1];
        }
        
        return a;
    }
    
    public void removeHorizontalSeam(int[] a) {
        validateRange(a, width(), height()-1);
        Picture newPic = new Picture(width(), height()-1);
        double[][] newE = new double[width()][height()-1];
        
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < a[x]) {
                    newPic.set(x, y, picture.get(x, y));
                    newE[x][y] = E[x][y];
                } else if (y > a[x]) {
                    newPic.set(x, y-1, picture.get(x, y));
                    newE[x][y-1] = E[x][y];
                }
            }
        }
        
        picture = newPic;
        E = newE;
        
        for (int x = 0; x < width(); x++) {
            if (a[x]-1 >= 0) {
                E[x][a[x]-1] = energy(x, a[x]-1);
            }
            
            if (a[x] < height()) {
                E[x][a[x]] = energy(x, a[x]);
            }
        }
    }
    
    public void removeVerticalSeam(int[] a) {
        validateRange(a, height(), width()-1);
        Picture newPic = new Picture(width()-1, height());
        double[][] newE = new double[width()-1][height()];
        
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (x < a[y]) {
                    newPic.set(x, y, picture.get(x, y));
                    newE[x][y] = E[x][y];
                } else if (x > a[y]) {
                    newPic.set(x-1, y, picture.get(x, y));
                    newE[x-1][y] = E[x][y];
                }
            }
        }
        
        picture = newPic;
        E = newE;
        
        for (int y = 0; y < height(); y++) {
            if (a[y]-1 >= 0) {
                E[a[y]-1][y] = energy(a[y]-1, y);
            }
            
            if (a[y] < width()) {
                E[a[y]][y] = energy(a[y], y);
            }
        }
    }
    
    private void validateRange(int[] a, int expectedLen, int maxVal) {
        if (a.length != expectedLen) {
            throw new java.lang.IllegalArgumentException("Wrong length");
        }
        
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0 || a[i] > maxVal) {
                throw new java.lang.IllegalArgumentException("Out of bound");
            }
            
            if (i > 0 && Math.abs(a[i-1] - a[i]) > 1) {
                throw new java.lang.IllegalArgumentException("Not connected");
            }
        }
    }
}