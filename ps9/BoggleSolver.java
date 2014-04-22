public class BoggleSolver
{
    private TrieSET trie;
    
    public BoggleSolver(String[] dictionary) {
        trie = new TrieSET();
        for (int i = 0; i < dictionary.length; i++) {
            String word = dictionary[i];
            if (word.length() > 2) {
                trie.add();
            }
        }
    }
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    }
    
    public int scoreOf(String word) {
        if (!trie.contains(word)) {
            return 0;
        }
        
        int score;
        switch (word.length()) {
            case 3: case 4:
                score = 1;
                break;
            case 5:
                score = 2;
                break;
            case 6:
                score = 3;
                break;
            case 7:
                score = 5;
                break;
            default:
                score = 11;
                break;
        }
        
        return score;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}