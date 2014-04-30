import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> tree;
    
    public PointSET() {
        tree = new TreeSet<Point2D>();
    }
    
    public boolean isEmpty() {
        return tree.isEmpty();
    }
    
    public int size() {
        return tree.size();
    }
    
    public void insert(Point2D p) {
        tree.add(p);
    }
    
    public boolean contains(Point2D p) {
        return tree.contains(p);
    }
    
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : tree) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }
        
        return q;
    }
    
    public Point2D nearest(Point2D p) {
        Point2D best = null;
        double shortest = -1;
        
        for (Point2D p2 :tree) {
            double d = p.distanceSquaredTo(p2);
            if (shortest == -1 || shortest > d) {
                shortest = d;
                best = p2;
            }
        }
        
        return best;
    }
}