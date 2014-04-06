import java.util.Hashtable;
import java.util.Arrays;

public class BaseballElimination {
    private final int numberOfTeams;
    private final String[] teams;
    private final Hashtable<String, Integer> team2id;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teams = new String[numberOfTeams];
        team2id = new Hashtable<String, Integer>();
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        against = new int[numberOfTeams][numberOfTeams];
        int teamId = 0;
        
        while (!in.isEmpty()) {
            String team = in.readString();
            teams[teamId] = team;
            team2id.put(team, teamId);
            
            wins[teamId] = in.readInt();
            losses[teamId] = in.readInt();
            remaining[teamId] = in.readInt();
            
            for (int i = 0; i < numberOfTeams; i++) {
                against[teamId][i] = in.readInt();
            }
            
            teamId++;
        }
    }
    
    public int numberOfTeams() {
        return numberOfTeams;
    }
    
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }
    
    public int wins(String team) {
        return wins[team2id.get(team)];
    }
    
    public int losses(String team) {
        return losses[team2id.get(team)];
    }
    
    public int remaining(String team) {
        return remaining[team2id.get(team)];
    }
    
    public int against(String team1, String team2) {
        return against[team2id.get(team1)][team2id.get(team2)];
    }
    
    public boolean isEliminated(String team) {
        return false;
    }
    
    public Iterable<String> certificateOfElimination(String team) {
        return new Queue<String>();
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