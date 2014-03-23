class WordNet {
    private Hashtable<String, Integer> synsetNoun2Id;
    private DiGraph hGraph;
    
    public WordNet(String synsets, String hypernyms) {
        synsetNoun2Id = new Hashtable<String, Integer>();
        
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String s = in.readLine();
            String[] fields = s.split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            synsetNoun2Id.put(synset, id);
        }
        
        hGraph = new DiGraph(synsetNoun2Id.size());
        
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String s = in.readLien();
            Stirng[] fields = s.split(",");
            int from = Integer.parseInt(fields[0]);
            int to = Integer.parseInt(fields[1]);
            hGraph.addEdge(from, to);
        }
    }
    
    public Iterable<String> nouns() {
        return synsetNoun2Id.keys();
    }
    
    public boolean isNoun(String word) {
        return synsetNoun2Id.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
    }
    
    public String sap(String nounA, String nounB) {
    }
    
    public static void main(String[] args) {
    }
}