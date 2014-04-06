import java.util.Hashtable;
import java.util.Arrays;

public class BaseballElimination {
    private int numberOfTeams;
    private String[] teams;
    private Hashtable<String, Integer> team2id;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private boolean[] eliminated;
    private Bag<String>[] certificates;
    private int leaderId;
    
    public BaseballElimination(String filename) {
        loadFile(filename);
        loadLeaderId();
        
        eliminated = new boolean[numberOfTeams];
        certificates = (Bag<String>[]) new Bag[numberOfTeams];
        
        for (int teamId = 0; teamId < numberOfTeams; teamId++) {
            if (wins[teamId] + remaining[teamId] < wins[leaderId]) {
                eliminated[teamId] = true;
                certificates[teamId] = new Bag<String>();
                certificates[teamId].add(teams[leaderId]);
                continue;
            }
            
            int V = 2 + (numberOfTeams-1)*(numberOfTeams-2)/2 + (numberOfTeams-1);
            int t = V - 1;
            int s = V - 2;
            int v0 = 0;
            int v1 = (numberOfTeams-1)*(numberOfTeams-2)/2;
            int fullCapacity = 0;
            FlowNetwork G = new FlowNetwork(V);
            for (int i = 0; i < numberOfTeams; i++) {
                if (i == teamId) {
                    continue;
                }
                
                for (int j = i+1; j < numberOfTeams; j++) {
                    if (j == teamId) {
                        continue;
                    }
                    
                    fullCapacity += against[i][j];
                    FlowEdge e1 = new FlowEdge(s, v0, (double) against[i][j], 0d);
                    FlowEdge e2 = new FlowEdge(v0, getTeamVertex(i, v1, teamId), 
                                               Double.POSITIVE_INFINITY, 0d);
                    FlowEdge e3 = new FlowEdge(v0, getTeamVertex(j, v1, teamId), 
                                               Double.POSITIVE_INFINITY, 0d);
                    G.addEdge(e1);
                    G.addEdge(e2);
                    G.addEdge(e3);
                    v0++;
                }
                
                FlowEdge e = 
                    new FlowEdge(getTeamVertex(i, v1, teamId), 
                                 t, 
                                 (double) (wins[teamId]+remaining[teamId]-wins[i]),
                                 0d);
                G.addEdge(e);
            }
            
            FordFulkerson ff = new FordFulkerson(G, s, t);
            
            if (ff.value() == fullCapacity) {
                eliminated[teamId] = false;
            } else {
                eliminated[teamId] = true;
                certificates[teamId] = new Bag<String>();
                
                for (int i = 0; i < numberOfTeams; i++) {
                    if (i != teamId) {
                        int v = getTeamVertex(i, v1, teamId);
                        if (ff.inCut(v)) {
                            certificates[teamId].add(teams[i]);
                        }
                    }
                }
            }
        }
    }
    
    private int getTeamVertex(int teamId, int base, int teamIdToEliminate) {
        if (teamId < teamIdToEliminate) {
            return base + teamId;
        } else {
            return base + teamId - 1;
        }
    }

    private void loadLeaderId() {
        int leadingWins = 0;
        for (int teamId = 0; teamId < numberOfTeams; teamId++) {
            if (wins[teamId] > leadingWins) {
                leadingWins = wins[teamId];
                leaderId = teamId;
            }
        }
    }
    
    private void loadFile(String filename) {
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
        validateTeam(team);
        return wins[team2id.get(team)];
    }
    
    public int losses(String team) {
        validateTeam(team);
        return losses[team2id.get(team)];
    }
    
    public int remaining(String team) {
        validateTeam(team);
        return remaining[team2id.get(team)];
    }
    
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return against[team2id.get(team1)][team2id.get(team2)];
    }
    
    public boolean isEliminated(String team) {
        validateTeam(team);
        return eliminated[team2id.get(team)];
    }
    
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return certificates[team2id.get(team)];
    }
    
    private void validateTeam(String team) {
        if (!team2id.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("Bad team");
        }
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