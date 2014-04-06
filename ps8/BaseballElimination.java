public class BaseballElimination {
    public BaseballElimination(String filename) {
    }
    
    public int numberOfTeams() {
    }
    
    public Iterable<String> teams() {
    }
    
    public int wins(String team) {
    }
    
    public int losses(String team) {
    }
    
    public int remaining(String team) {
    }
    
    public int against(String team1, String team2) {
    }
    
    public boolean isEliminated(String team) {
    }
    
    public Iterable<String> certificateOfElimination(String team) {
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}