public class SAP {
    private Digraph G;
    
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }
    
    public int length(int v, int w) {
        return length(getIter(v), getIter(w));
    }
    
    public int ancestor(int v, int w) {
        return ancestor(getIter(v), getIter(w));
    }
    
    private Iterable<Integer> getIter(int v) {
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(v);
        return q;
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w, true);
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w, false);
    }
    
    private int bfs(Iterable<Integer> v, Iterable<Integer> w, boolean getLength) {
        int[] pathToV = new int[G.V()];
        int[] pathToW = new int[G.V()];
        java.util.Arrays.fill(pathToV, -1);
        java.util.Arrays.fill(pathToW, -1);
        Queue<Integer> checkV = new Queue<Integer>();
        Queue<Integer> checkW = new Queue<Integer>();
        
        for (int v0 : v) {
            pathToV[v0] = 0;
            checkV.enqueue(v0);
        }
        
        for (int w0 : w) {
            pathToW[w0] = 0;
            checkW.enqueue(w0);
        }
        
        int ancestor = -1;
        int shortestPath = -1;
        int iterations = 0;
        
        while (!checkV.isEmpty() || !checkW.isEmpty()) {
            Queue<Integer> newCheckV = new Queue<Integer>();
            Queue<Integer> newCheckW = new Queue<Integer>();
            
            for (int x : checkV) {
                if (pathToW[x] != -1 && (shortestPath == -1 || pathToV[x] + pathToW[x] < shortestPath)) {
                    shortestPath = pathToV[x] + pathToW[x];
                    ancestor = x;
                }
                
                for (int y : G.adj(x)) {
                    if (pathToV[y] == -1) {
                        pathToV[y] = pathToV[x] + 1;
                        newCheckV.enqueue(y);
                    }
                }
            }
            
            for (int x : checkW) {
                if (pathToV[x] != -1 && (shortestPath == -1 || pathToV[x] + pathToW[x] < shortestPath)) {
                    shortestPath = pathToV[x] + pathToW[x];
                    ancestor = x;
                }
                
                for (int y : G.adj(x)) {
                    if (pathToW[y] == -1) {
                        pathToW[y] = pathToW[x] + 1;
                        newCheckW.enqueue(y);
                    }
                }
            }
            
            checkV = newCheckV;
            checkW = newCheckW;
            iterations++;
            if (shortestPath != -1 && shortestPath <= iterations) {
                break;
            }
        }
        
        if (getLength) {
            return shortestPath;
        } else {
            return ancestor;
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}