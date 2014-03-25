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
        
        S = new SAP(G);
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
            throw new java.lang.NullPointerException();
        }
    }
    
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        return id2Synset.get(S.ancestor(noun2Ids.get(nounA), noun2Ids.get(nounB)));
    }
    
    public static void main(String[] args) {
    }
}