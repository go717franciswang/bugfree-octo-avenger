public class Solver {
    private int moves;
    private boolean isSolvable;
    private Stack<Board> solution;
    private MinPQ<Node> pq;
    private MinPQ<Node> pqTwin;
    
    private class Node implements Comparable<Node> {
        Board board;
        Node previous;
        int moves;
        
        public int compareTo(Node other) {
            /*
            int hammingA = board.hamming()+moves;
            int hammingB = other.board.hamming()+other.moves;
            if (hammingA > hammingB) {
                return 1;
            } else if (hammingA < hammingB) {
                return -1;
            }
            */
            
            int manhattanA = board.manhattan()+moves;
            int manhattanB = other.board.manhattan()+other.moves;
            if (manhattanA > manhattanB) {
                return 1;
            } else if (manhattanA < manhattanB) {
                return -1;
            }
            
            return 0;
        }
    }
    
    public Solver(Board initial) {
        Node first = new Node();
        first.board = initial;
        first.moves = 0;
        pq = new MinPQ<Node>();
        pq.insert(first);
        
        Node firstTwin = new Node();
        firstTwin.board = initial.twin();
        firstTwin.moves = 0;
        pqTwin = new MinPQ<Node>();
        pqTwin.insert(firstTwin);
        
        solve();
    }
    
    private void solve() {
        while (true) {
            Node last = pq.delMin();
            Node lastTwin = pqTwin.delMin();
            
            if (last.board.isGoal()) {
                isSolvable = true;
                moves = last.moves;
                Node node = last;
                Stack<Board> solution = new Stack<Board>();
                while (node != null) {
                    solution.push(node.board);
                    node = node.previous;
                }
                this.solution = solution;
                
                return;
            }
            
            if (lastTwin.board.isGoal()) {
                isSolvable = false;
                moves = -1;
                solution = null;
                
                return;
            }
            
            for (Board neighbor : last.board.neighbors()) {
                if (last.previous == null || !neighbor.equals(last.previous.board)) {
                    Node node = new Node();
                    node.board = neighbor;
                    node.previous = last;
                    node.moves = last.moves+1;
                    pq.insert(node);
                }
            }
            
            for (Board neighbor : lastTwin.board.neighbors()) {
                if (lastTwin.previous == null || !neighbor.equals(lastTwin.previous.board)) {
                    Node node = new Node();
                    node.board = neighbor;
                    node.previous = lastTwin;
                    node.moves = lastTwin.moves+1;
                    pqTwin.insert(node);
                }
            }
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }
    
    public int moves() {
        return moves;
    }
    
    public Iterable<Board> solution() {
        return solution;
    }
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}