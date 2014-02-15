import java.util.Arrays;

public class Brute {
    public static void main(String[] args) {
        In f = new In(args[0]);
        String line;
        
        line = f.readLine().trim();
        int lineCount = Integer.parseInt(line);
        Point[] points = new Point[lineCount];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        
        for (int i = 0; i < lineCount; i++) {
            line = f.readLine().trim();
            String[] parts = line.split("\\s+");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            
            points[i] = new Point(x, y);
            points[i].draw();
        }
        
        Arrays.sort(points);
        
        for (int i = 0; i < points.length; i++) {
            Point a = points[i];
            for (int j = i+1; j < points.length; j++) {
                Point b = points[j];
                double m = a.slopeTo(b);
                
                for (int k = j+1; k < points.length; k++) {
                    Point c = points[k];
                    if (m != a.slopeTo(c)) {
                        continue;
                    }
                    
                    for (int q = k+1; q < points.length; q++) {
                        Point d = points[q];
                        if (m != a.slopeTo(d)) {
                            continue;
                        }
                        
                        StdOut.println(a + " -> " + b + " -> " + c + " -> " + d);
                        a.drawTo(d);
                    }
                }
            }
        }
    }
}