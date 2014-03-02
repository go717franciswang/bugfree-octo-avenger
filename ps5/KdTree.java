import java.util.TreeSet;

public class KdTree {
    private Node root;
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
        size = 0;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void insert(Point2D p) {
        root = insert(root, p, true);
        size++;
    }
    
    private Node insert(Node node, Point2D p, boolean useX) {
        if (node == null) return new Node(p);
        
        Point2D p1 = node.point;
        if (p1.equals(p)) {
            // looks like the assignment does not allow the same key
            // so offset the later increase in size 
            size--;
            return node;
        }
        
        if (useX) {
            if (p.x() < p1.x()) {
                node.left = insert(node.left, p, !useX);
            } else {            
                node.right = insert(node.right, p, !useX);
            }
        } else {
            if (p.y() < p1.y()) {
                node.left = insert(node.left, p, !useX);    
            } else {
                node.right = insert(node.right, p, !useX);
            }
        }
        
        return node;
    }
    
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }
    
    private boolean contains(Node node, Point2D p, boolean useX) {
        if (node == null) return false;
        
        Point2D p2 = node.point;
        if (p2.equals(p)) return true;
        if (useX && p.x() < p2.x() || !useX && p.y() < p2.y()) {
            return contains(node.left, p, !useX);
        } else {           
            return contains(node.right, p, !useX);
        }
    }
    
    public void draw() {        
        draw(root, true, 0, 0, 1, 1);
    }
    
    private void draw(Node node, boolean useX, 
                      double xmin, double ymin, double xmax, double ymax) {
        if (node == null) return;
        Point2D p = node.point;
        p.draw();
        StdOut.println(xmin + ", " + ymin + ", " + xmax + ", " + ymax);
        if (useX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.x(), ymin, p.x(), ymax);
            draw(node.left, !useX, xmin, ymin, p.x(), ymax);
            draw(node.right, !useX, p.x(), ymin, xmax, ymax);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, p.y(), xmax, p.y());
            draw(node.left, !useX, xmin, ymin, xmax, p.y());
            draw(node.right, !useX, xmin, p.y(), xmax, ymax);
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
        if (useX) {
            if (rect.intersects(new RectHV(xmin, ymin, node.point.x(), ymax))) {
                range(node.left, rect, q, !useX, xmin, ymin, node.point.x(), ymax);
            }
            
            if (rect.intersects(new RectHV(node.point.x(), ymin, xmax, ymax))) {
                range(node.right, rect, q, !useX, node.point.x(), ymin, xmax, ymax);
            }
        } else {
            if (rect.intersects(new RectHV(xmin, ymin, xmax, node.point.y()))) {
                range(node.left, rect, q, !useX, xmin, ymin, xmax, node.point.y());
            }
            
            if (rect.intersects(new RectHV(xmin, node.point.y(), xmax, ymax))) {
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
        double shortest = p.distanceSquaredTo(node.point);
        Point2D p1 = node.point;
        Point2D best = p1;
        
        if (useX) {
            if (p.x() < node.point.x()) {
                RectHV rect = new RectHV(xmin, ymin, p1.x(), ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.left, p, !useX, xmin, ymin, p1.x(), ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
                
                rect = new RectHV(p1.x(), ymin, xmax, ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.right, p, !useX, p1.x(), ymin, xmax, ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
            } else {
                RectHV rect = new RectHV(p1.x(), ymin, xmax, ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.right, p, !useX, p1.x(), ymin, xmax, ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
                
                rect = new RectHV(xmin, ymin, p1.x(), ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.left, p, !useX, xmin, ymin, p1.x(), ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
            }
        } else {
            if (p.y() < node.point.y()) {
                RectHV rect = new RectHV(xmin, ymin, xmax, p1.y());
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.left, p, !useX, xmin, ymin, xmax, p1.y());
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
                
                rect = new RectHV(xmin, p1.y(), xmax, ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.right, p, !useX, xmin, p1.y(), xmax, ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
            } else {
                RectHV rect = new RectHV(xmin, p1.y(), xmax, ymax);
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.right, p, !useX, xmin, p1.y(), xmax, ymax);
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
                
                rect = new RectHV(xmin, ymin, xmax, p1.y());
                if (rect.distanceSquaredTo(p) < shortest) {
                    Point2D p2 = nearest(node.left, p, !useX, xmin, ymin, xmax, p1.y());
                    if (p2 != null) {
                        double d = p2.distanceSquaredTo(p);
                        if (d < shortest) {
                            shortest = d;
                            best = p2;
                        }
                    }
                }
            }
        }
            
        return best;
    }
}