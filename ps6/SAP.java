class SAP {
    private Digraph G;
    
    public SAP(Digraph G) {
        this.G = G;
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
        int[] groupIds = new int[G.V()];
        java.util.Arrays.fill(groupIds, 0);
        int[] pathTo = new int[G.V()];
        int[] edgeTo = new int[G.V()];
        Queue<Integer> check = new Queue<Integer>();
        
        for (int v0 : v) {
            for (int w0 : w) {
                if (v0 == w0) {
                    if (getLength) {
                        return 0;
                    } else {
                        return v0;
                    }
                }
            }
        }
        
        for (int v0 : v) {
            groupIds[v0] = 1;
            pathTo[v0] = 0;
            edgeTo[v0] = v0;
            check.enqueue(v0);
        }
        
        for (int w0 : w) {
            groupIds[w0] = -1;
            pathTo[w0] = 0;
            edgeTo[w0] = w0;
            check.enqueue(w0);
        }
        
        while (!check.isEmpty()) {
            int x = check.dequeue();
            int xgroupId = groupIds[edgeTo[x]];
            
            for (int y : G.adj(x)) {
                if (groupIds[y] == 0) {
                    check.enqueue(y);
                    edgeTo[y] = x;
                    groupIds[y] = xgroupId;
                    pathTo[y] = pathTo[x] + 1;
                } else if (groupIds[y] == -xgroupId) {
                    if (getLength) {
                        return pathTo[x] + pathTo[y] + 1;
                    } else {
                        return y;
                    }
                }
            }
        }
        
        return -1;
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