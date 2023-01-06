package LaLiga;

import structures.SingleLinkedList;

/**
 * @author Antonio Gabarrús Nerin (alu142920)
 * @description Class representation of a Team. The teams can be compared with each other what <br>
 * helps to order them (Standings)
 */
public class Team implements Comparable<Team>{

    private final String name;
    private final String shortName;
    private int budget;
    private SingleLinkedList players;
    private int wins;
    private int draws;
    private int losses;
    private int points;
    private int goalsFor;
    private int goalsAgainst;
    
    
    private boolean active;
    private boolean hasPlayedOnSunday;

    

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param name The name of the football team
     * @description Creation of the team with the name given. All its stats are initialized to 0 
     * and a short name is created for it
     */
    public Team(String name) {
        this.name = name;
        this.budget = 0;
        this.players = new SingleLinkedList();
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.points = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.hasPlayedOnSunday = false;
        this.active = true;
        //If no short name is given, we need to set one
        int i = 0;
        String shorterName = "";
        for (int j = 0; i < this.name.length() && i < 3; j++){
            if(this.name.charAt(j) != (' ')){
                shorterName += this.name.charAt(j);
                ++i;
            }
        } 
        this.shortName = shorterName;
        
    }
    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param name The name of the football team
     * @param shortName The short name to be desplayed on matches
     * @description Creation of the team with the name and the short name given. 
     */
    public Team(String name, String shortName){
        this.name = name;
        this.shortName = shortName;
        this.budget = 0;
        this.players = new SingleLinkedList();
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.points = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.hasPlayedOnSunday = false;
        this.active = true;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @description Adds a win and the corresponding points to the team
     */
    public void win() {
        this.wins++;
        this.points += 3;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @description Adds a draw and the corresponding points to the team
     */
    public void draw() {
        this.draws++;
        this.points++;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @description Adds a loss and no points to the team
     */
    public void loss() {
        this.losses++;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The corresponding points of the team
     */
    public int getPoints() {
        return points;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The budget of the team, being that the sum of the cost of each player
     */
    public int getBudget() {
        return budget;
    }
    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @description Adds a player to the team if the player is not already playing for the team
     * and if the team has not a full team. It also increases the budget if the player is added
     * @return true if the player has been added to the team, false otherwise
     */
    public boolean addPlayer(Player p){
        if (this.players.contains(p) || this.players.size >= 11) return false;
        this.players.insertHead(p);
        this.budget += p.getCost();
        return true;
    }
    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @description Removes a player from the list of players of the team and lowers the budget  
     * @return The removed player if any. If there is no sucha player, this method returns null
     */
    public Player removePlayer(Player p){
        Player removed = (Player) this.players.remove(p);
        if(removed!=null) this.budget -= removed.getCost();
        return removed;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param goalsAgainst Amount of goals that the team has received
     * @description Adds the amount of goals to the quantity of goals against
     */
    public void addGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst += goalsAgainst;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param goalsFor Amount of goals that the team has scored
     * @description Adds the amount of goals to the quantity of goals for
     */
    public void addGoalsFor(int goalsFor) {
        this.goalsFor += goalsFor;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The amount of goals received by this team
     */
    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The the amount of goals scored by this team
     */
    public int getGoalsFor() {
        return goalsFor;
    }
    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The goals difference of the team calculated as Goals For - Goals Against 
     */
    public int getGoalsDifference() {
        return goalsFor - goalsAgainst;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The short name of the team
     */
    public String getShortName() {
        return shortName;
    }
    
    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param hasPlayedOnSunday 
     * @description Set this team hasPlayedOnSunday as indicated by the parameter passed
     */
    public void setHasPlayedOnSunday(boolean hasPlayedOnSunday) {
        this.hasPlayedOnSunday = hasPlayedOnSunday;
    }

    
    /**
     * Compares this Team with Team 'other' and returns -1 if this team is found first on standings, 
     * 0 if it does not matter, and 1 if it is found second on the standings
     * @param other The Team with which we are comparing this Team
     * @return -1 if is lower, 0 if equals or 1 if higher. Note that, in this 
     * context, being lower is the same as been found first on the standings table.
     * <br> So what this means is if the position(index) on the standings is lower equal or higher
     */
    @Override
    public int compareTo(Team other) {
        if(this.points > other.points)return -1;
        if(this.points < other.points) return 1;
        if(this.getGoalsDifference() > other.getGoalsDifference()) return -1;
        if(this.getGoalsDifference() < other.getGoalsDifference()) return 1;
        if(this.getGoalsFor() > other.getGoalsFor()) return -1;
        if(this.getGoalsFor() < other.getGoalsFor()) return 1;
        return 0;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return true if team is active, false otherwise (needed for the implementation of the hash map)
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return true if last match was on Sunday, false otherwise
     */
    public boolean hasPlayedOnSunday() {
        return hasPlayedOnSunday;
    }

    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @param active
     * @description Set the status of the team as indicates param active 
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    
    /**
     * @author Antonio Gabarrús Nerin (alu142920)
     * @return The Team representation as a String
     */
    @Override
    public String toString() {
        return "Team{" + name + ", wins=" + wins + ", draws=" + draws + ", losses=" + losses + ", points=" + points + ", goalsFor=" + goalsFor + ", goalsAgainst=" + goalsAgainst + ", goalsDifference=" + getGoalsDifference() + '}';
    }
    
    
    
    
    
    
}
