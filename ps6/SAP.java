class SAP {
    private Digraph G;
    
    public SAP(Digraph G) {
        this.G = G;
    }
    
    public int length(int v, int w) {
    }
    
    public int ancestor(int v, int w) {
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
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