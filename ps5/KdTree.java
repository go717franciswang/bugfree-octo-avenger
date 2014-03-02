import java.util.TreeSet;

public class KdTree {
    private Node parent;
    private int size;
    
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        
        public Node(Point2D point) {
            this.point = point;
        }
    }
    
    public KdTree() {
        parent = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return parent == null;
    }
    
    public int size() {
        return size;
    }
    
    public void insert(Point2D p) {
        root = insert(root, p, true);
    }
    
    private Node insert(Node node, Point2D p, boolean useX) {
        if (node == null) return Node(p);
        if (useX) {
            if (node.point.x() < p.x()) {
                return insert(node.left, p, !useX);
            }
            
            return insert(node.right, p, !useX);
        }
        
        if (node.point.y() < p.y()) {
            return insert(node.left, p, !useX);
        }
        
        return insert(node.right, p, !useX);
    }
    
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }
    
    private boolean contains(Node node, Point2D p, boolean useX) {
        if (node == null) return false;
        if (node.point == p) return true;
        if (useX) {
            if (node.point.x() < p.x()) {
                return contains(node.left, p, !useX);
            }
            
            return contains(node.right, p, !useX);
        }
        
        if (node.point.y() < p.y()) {
            return contains(node.left, p, !useX);
        }
        
        return contains(node.right, p, !useX);
    }
    
    public void draw() {Queue<Node> q = new Queue<Node>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            node = q.dequeue();
            if (node == null) continue;
            
            node.point.draw();
            q.enqueue(node.left);
            q.enqueue(node.right);
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q, true, 0, 0, 1, 1);
        return q;   
    }
    
    private void range(Node node, RectHV rect, Queue<Point2D> q, boolean useX, 
                       double xmin, double ymin, double xmax, double ymax) {
        if (node == null) return;
        if (rect.contains(node.point)) q.enqueue(node.point);
        if (node.useX) {
            if (rect.intersect(new RectHV(xmin, ymin, node.point.x(), ymax))) {
                range(node.left, rect, q, !useX, xmin, ymin, node.point.x(), ymax);
            }
            
            if (rect.intersect(new RectHV(node.point.x(), ymin, xmax, ymax))) {
                range(node.right, rect, q, !useX, node.point.x(), ymin, xmax, ymax);
            }
        } else {
            if (rect.intersect(new RectHV(xmin, ymin, xmax, node.point.y()))) {
                range(node.left, rect, q, !useX, xmin, ymin, xmax, node.point.y());
            }
            
            if (rect.intersect(new RectHV(xmin, node.point.y(), xmax, ymax))) {
                range(node.right, rect, q, !useX, xmin, node.point.y(), xmax, ymax);
            }
        }
    }
    
    public Point2D nearest(Point2D p) {
        return nearest(root, p, true, 0, 0, 1, 1);
    }
    
    private Point2D nearest(Node node, Point2D p, boolean useX,
                            double xmin, double ymin, double xmax, double ymax) {
        if (node == null) return null;
        if (node.useX) {
            if (p.x() < 
    }
}