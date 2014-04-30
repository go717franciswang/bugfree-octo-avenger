import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;

public class WordNet {
    private Hashtable<String, Queue<Integer>> noun2Ids;
    private int lastSynsetId;
    private ArrayList<String> id2Synset;
    private Digraph G;
    private SAP S;
    private int root;
    
    public WordNet(String synsets, String hypernyms) {
        noun2Ids = new Hashtable<String, Queue<Integer>>();
        id2Synset = new ArrayList<String>();
        
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String s = in.readLine().trim();
            if (s.length() > 0) {
                String[] fields = s.split(",");
                int id = Integer.parseInt(fields[0]);
                lastSynsetId = id;
                id2Synset.add(fields[1]);
                String[] synset = fields[1].split(" ");
                for (int i=0; i<synset.length; i++) {
                    String noun = synset[i];
                    if (noun2Ids.get(noun) == null) {
                        noun2Ids.put(noun, new Queue<Integer>());
                    }
                    
                    noun2Ids.get(noun).enqueue(id);
                }
            }
        }
        
        G = new Digraph(lastSynsetId+1);
        
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String s = in.readLine().trim();
            if (s.length() > 0) {
                String[] fields = s.split(",");
                int from = Integer.parseInt(fields[0]);
                for (int i=1; i<fields.length; i++) {
                    int to = Integer.parseInt(fields[i]);
                    G.addEdge(from, to);
                }
            }
        }
        
        assertRootedDAG(G);        
        S = new SAP(G);
    }
    
    private void dfs(int x, boolean[] discovered, Digraph G, Stack<Integer> ordering) {
        discovered[x] = true;
        if (!G.adj(x).iterator().hasNext()) {
            if (root == -1) {
                root = x;
            } else if (root != x) {
                throw new IllegalArgumentException("Ambiguous root");
            }
        } else {
            for (int y : G.adj(x)) {
                if (!discovered[y]) {
                    dfs(y, discovered, G, ordering);
                }
            }
        }
        
        ordering.push(x);
    }
    
    private void assertRootedDAG(Digraph G) {
        boolean[] discovered = new boolean[G.V()];
        java.util.Arrays.fill(discovered, false);
        Stack<Integer> ordering = new Stack<Integer>();
        root = -1;
        int steps = 0;
        
        for (int synsetId = 0; synsetId <= lastSynsetId; synsetId++) {
            if (!discovered[synsetId]) {
                dfs(synsetId, discovered, G, ordering);
            }
        }
        
        java.util.Arrays.fill(discovered, false);
        for (int x : ordering) {
            discovered[x] = true;
            for (int y : G.adj(x)) {
                if (discovered[y]) {
                    throw new IllegalArgumentException("Invalid topological ordering");
                }
            }
        }
    }
    
    public Iterable<String> nouns() {
        return Collections.list(noun2Ids.keys());
    }
    
    public boolean isNoun(String word) {
        return noun2Ids.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        return S.length(noun2Ids.get(nounA), noun2Ids.get(nounB));
    }
    
    private void checkNouns(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Bad noun");
        }
    }
    
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        return id2Synset.get(S.ancestor(noun2Ids.get(nounA), noun2Ids.get(nounB)));
    }
    
    public static void main(String[] args) {
    }
}