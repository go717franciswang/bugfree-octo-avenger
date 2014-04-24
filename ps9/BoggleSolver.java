import java.util.HashSet;

public class BoggleSolver
{
    private TST<Boolean> trie;
    
    public BoggleSolver(String[] dictionary) {
        trie = new TST<Boolean>();
        for (int i = 0; i < dictionary.length; i++) {
            String word = dictionary[i];
            if (word.length() > 2) {
                trie.put(word, true);
            }
        }
    }
    
    private boolean isValidWord(String word) {
        return trie.contains(word);
    }
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> validWords = new HashSet<String>();
        boolean[][] discovered = new boolean[board.rows()][board.cols()];
        
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                StringBuilder chars = new StringBuilder();
                dfs(board, i, j, chars, trie.root, 0, discovered, validWords);
            }
        }
        return validWords;
    }
    
    private void dfs(BoggleBoard board, int row, int col, StringBuilder chars, TST<Boolean>.Node node, int d,
                     boolean[][] discovered, HashSet<String> validWords) {
        discovered[row][col] = true;
        char letter = board.getLetter(row, col);
        addLetter(chars, letter);
        
        String word = chars.toString();
        
        TST<Boolean>.Node x = trie.get(node, word, d);
        if (x == null) {
            removeLetter(chars, letter);
            discovered[row][col] = false;
            return;
        }
        
        if (x.val != null) {
            validWords.add(word);
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row - i;
                int c = col - j;
                if (r < 0 || c < 0 || r >= board.rows() || c >= board.cols() || 
                    discovered[r][c]) {
                    continue;
                }
                
                if (letter == 'Q') {
                    dfs(board, r, c, chars, x.mid, d + 2, discovered, validWords);
                } else {
                    dfs(board, r, c, chars, x.mid, d + 1, discovered, validWords);
                }
            }
        }
        
        removeLetter(chars, letter);
        discovered[row][col] = false;
    }
    
    private void addLetter(StringBuilder chars, char letter) {
        chars.append(letter);
        if (letter == 'Q') {
            chars.append('U');
        }
    }
    
    private void removeLetter(StringBuilder chars, char letter) {
        chars.deleteCharAt(chars.length() - 1);
        if (letter == 'Q') {
            chars.deleteCharAt(chars.length() - 1);
        }
    }
    
    public int scoreOf(String word) {
        if (!isValidWord(word)) {
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
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        
        double startTime = System.currentTimeMillis();
        int iters = 0;
        while (true) {
            BoggleBoard b = new BoggleBoard();
            solver.getAllValidWords(b);
            iters++;
            double endTime = System.currentTimeMillis();
            if (endTime - startTime > 1000) {
                break;
            }
        }
        StdOut.println("Solved " + iters + " in 1 second");
    }
}