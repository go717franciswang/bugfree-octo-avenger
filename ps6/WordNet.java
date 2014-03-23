import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;

class WordNet {
    private Hashtable<String, Integer> noun2Id;
    private int lastSynsetId;
    private String[] id2Noun;
    private Digraph G;
    private SAP S;
    
    public WordNet(String synsets, String hypernyms) {
        noun2Id = new Hashtable<String, Integer>();
        
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String s = in.readLine().trim();
            if (s.length() > 0) {
                String[] fields = s.split(",");
                int id = Integer.parseInt(fields[0]);
                lastSynsetId = id;
                String[] synset = fields[1].split(" ");
                for (int i=0; i<synset.length; i++) {
                    noun2Id.put(synset[i], id);
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
        
        String[] id2Noun = new String[lastSynsetId+1];
        for (String noun : this.nouns()) {
            id2Noun[noun2Id.get(noun)] = noun;
        }
    }
    
    public Iterable<String> nouns() {
        return Collections.list(noun2Id.keys());
    }
    
    public boolean isNoun(String word) {
        return noun2Id.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
        return S.length(noun2Id.get(nounA), noun2Id.get(nounB));
    }
    
    public String sap(String nounA, String nounB) {
        return id2Noun[S.ancestor(noun2Id.get(nounA), noun2Id.get(nounB))];
    }
    
    public static void main(String[] args) {
    }
}