public class Outcast {
    private WordNet wordnet;
    
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        int[] d = new int[nouns.length];
        java.util.Arrays.fill(d, 0);
        
        for (int i=0; i<nouns.length; i++) {
            for (int j=i+1; j<nouns.length; j++) {
                int distance = wordnet.distance(nouns[i], nouns[j]);
                d[i] += distance;
                d[j] += distance;
            }
        }
        
        String o = nouns[0];
        int dmax = d[0];
        for (int i=0; i<nouns.length; i++) {
            if (d[i] > dmax) {
                dmax = d[i];
                o = nouns[i];
            }
        }
        
        return o;
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}