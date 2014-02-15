import java.util.Arrays;

public class Fast {
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
            String[] parts = line.split(" +");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            
            points[i] = new Point(x, y);
            points[i].draw();
        }
        
        Arrays.sort(points);
        Point[] copiedPoints = (Point[]) Arrays.copyOf(points, points.length);
        
        for (Point a : points) {
            Arrays.sort(copiedPoints, a.SLOPE_ORDER);
            int start = 0;
            int finish = 0;
            
            for (int j = 1; j < points.length; j++) {
                if (copiedPoints[0].slopeTo(copiedPoints[j]) != copiedPoints[0].slopeTo(copiedPoints[j-1])) {
                    processPoints(copiedPoints, start, finish);                        
                    start = j;
                }
                
                finish = j;
            }
            
            processPoints(copiedPoints, start, finish);
        }
    }
    
    private static void processPoints(Point[] points, int start, int finish) {
        if (finish - start >= 2) {
            if (isParentSmallest(points, start, finish)) {
                StdOut.print(points[0]);
                Point[] subset = (Point[]) Arrays.copyOfRange(points, start, finish+1);
                Arrays.sort(subset);
                
                for (Point p : subset) {
                    StdOut.print(" -> " + p);
                }
                StdOut.print("\n");
                points[0].drawTo(getLargest(points, start, finish));
            }
        }
    }
    
    private static Point getLargest(Point[] points, int start, int finish) {
        Point largest = points[start];
        for (int k = start+1; k <= finish; k++) {
            if (points[k].compareTo(largest) > 0) {
                largest = points[k];
            }
        }
        
        return largest;
    }
    
    private static boolean isParentSmallest(Point[] points, int start, int finish) {
        for (int k = start; k <= finish; k++) {
            if (points[0].compareTo(points[k]) >= 0) {
                return false;
            }
        }
        
        return true;
    }
}