package LaLiga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import structures.HashMap;
import structures.SingleLinkedList;

public class League {

    private static final int MAX_TEAMS = 21;

    private static final int TEAM_NAME = 0;
    private static final int TEAM_SHORT = 1;
    private static final int PLAYER_NAME = 0;
    private static final int PLAYER_AGE = 1;
    private static final int PLAYER_COST = 2;
    private static final int PLAYER_POSITION = 3;
    private static final int PLAYER_TEAM = 4;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK =  "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE =  "\u001B[34m";

    private SortingLibrary<Team> mySorter; // Library to sort teams
    
    private String name; // name of the league
    private SingleLinkedList matchdays; // matchdays of the whole league
    private HashMap teams; // hashmap to save the teams 

    /**
     * Default constructor for the league
     */
    public League(String name){
        this.matchdays = new SingleLinkedList();
        this.mySorter = new SortingLibrary<Team>();
        this.teams = new HashMap(MAX_TEAMS);
        this.name = name;
        this.loadTeamsAndPlayers();
    }

    /**
     * Constructor for the league
     * @param teams hashmap of teams
     */
    public League(HashMap teams, String name){
        this.matchdays = new SingleLinkedList();
        this.mySorter = new SortingLibrary<Team>();
        this.teams = teams;
        this.name = name;
    }

    /**
     * Method that adds a new team to the hash table and checks whether it is already contained
     * @param team team we want to aggregate
     * @return true if it is not contained and false if it is contained
     */
    public boolean addTeam(Team team){
        if(!teams.containsKey(team.getName())){
            teams.put(team);
            return true;
        }
        return false;
    }

   public void leagueSetup(Team[] myTeams, FileWriter logOut) throws IOException
   {
        //CircularList teamRotation = new CircularList();
        SingleLinkedList teamRotation = new SingleLinkedList();
        Team fixed = myTeams[0];
        for (int i = myTeams.length - 1; i > 0 ;--i){
            teamRotation.insertHead(myTeams[i]);
        }
        if (teamRotation.size%2 == 0) {
            teamRotation.insertHead(fixed);
            fixed = null;//The team facing null is the team that rests
        }
        Team restartTeam = (Team)teamRotation.first.data;
        int matchdayNumber = 1;
        
        
        do{
            Team[] homies = new Team[(myTeams.length%2==0)? myTeams.length/2:myTeams.length/2 + 1];
            Team[] aways = new Team[(myTeams.length%2==0)? myTeams.length/2:myTeams.length/2 + 1];
            homies[0] = fixed;
            for (int i = 0; i < teamRotation.size/2; i++){
                homies[i+1] = (Team)teamRotation.get(i);
            }
            //for(Team t : homies) System.out.print(t.getShortName() + "\t");
            //System.out.println();
            for (int i = teamRotation.size/2; i < teamRotation.size;i++){
                aways[i-teamRotation.size/2] = (Team)teamRotation.get(i);
            }
            //for(Team t : aways) System.out.print(t.getShortName() + "\t");
            //System.out.println();
            
            MatchDay matchday = new MatchDay(matchdayNumber++);
            
            for (int i = 0; i < homies.length; ++i){
                if(homies[i] != null && aways[i] == null) matchday.setRestingTeam(homies[i]);
                else if(homies[i] == null && aways[i] != null) matchday.setRestingTeam(aways[i]);
                else{
                    Match newMatch = new Match(homies[i], aways[i]);
                    if(matchday.addMatch(newMatch)) logOut.write("Match " + newMatch + " added");
                    else logOut.write("Match " + newMatch + " not added");
                    logOut.write("\n");
                }
            } 
            this.matchdays.insertLast(matchday);
            logOut.write("Matchday added. Size = " + this.matchdays.size + ". Number of matches of the new MatchDay = " + matchday.getNumberOfMatches() + "\n");
            teamRotation.insertHead(teamRotation.remove(teamRotation.get(teamRotation.size-1)));
        }while(teamRotation.first.data!=restartTeam);
        
        //First Leg correctly generated
        
        SingleLinkedList secondLeg = new SingleLinkedList();
        for (int i = 0; i < this.matchdays.size; i++){
            MatchDay secondLegMatchDay = ((MatchDay)this.matchdays.get(i)).cloneOpposite(i + this.matchdays.size + 1);
            secondLeg.insertHead(secondLegMatchDay);
        }
       this.matchdays.shuffle();
       secondLeg.shuffle();
       for (int i = 0; i < secondLeg.size; i++){
           this.matchdays.insertLast(secondLeg.get(i));
       }
       for(int i = 0; i < this.matchdays.size; i++){
           MatchDay matchday = (MatchDay) this.matchdays.get(i);
           matchday.setMatchdayNumber(i+1);
           matchday.setDayOfMatches();
       }
           
        
        
    }

    /**
     * Method which randomly shuffles a team array
     * @param myTeams the teams we want to shuffle
     */
    public void shuffle(Team[] myTeams, FileWriter logOut)throws IOException{
        if(myTeams!=null){
            Random rand = new Random(System.currentTimeMillis());
            for(int i=0; i<myTeams.length; i++){
                int swap = rand.nextInt(myTeams.length);
                logOut.write("Swapping position " + i + " with position " + swap + ".\n");
                Team aux = myTeams[i];
                myTeams[i] = myTeams[swap];
                myTeams[swap] = aux;
            }   
        }
    }

    /**
     * Method which loads all teams and players from a CSV file
     */
    private void loadTeamsAndPlayers(){
        try{
            Scanner scanTeams = new Scanner(new File("teams.csv"));
            Scanner scanPlayers = new Scanner(new File("players.csv"));
            scanTeams.nextLine(); //Skip headers
            while (scanTeams.hasNext()){
                String[] teamData = scanTeams.nextLine().split(";");
                this.addTeam(new Team(teamData[TEAM_NAME], teamData[TEAM_SHORT]));
            }
            scanTeams.close();
            //Teams loaded
            
            scanPlayers.nextLine();//Skip Headers
            while(scanPlayers.hasNext()){
                String[] playerData = scanPlayers.nextLine().split(";");
                teams.get(playerData[PLAYER_TEAM]).addPlayer(
                        new Player(playerData[PLAYER_NAME], 
                                Integer.parseInt(playerData[PLAYER_AGE]), 
                                playerData[PLAYER_POSITION], 
                                Integer.parseInt(playerData[PLAYER_COST])
                        )
                );
            }
            scanPlayers.close();
        } catch (FileNotFoundException ex){
            System.err.println("Exception thrown: " + ex.getMessage());
        }
    }

    
    

    /**
     * Method which displays the league standings
     */
	public void printStandings() {
        Team[] myTeams = mySorter.quickSortLL(this.teams.getValuesArray());
		System.out.println("                    ***** "+this.name+" *****");
		System.out.println("┌────────────────────────────────────────┬──┬──┬──┬──┬───┬───┬───┬───┐");
		System.out.format("│%1$-40s│%2$-2s│%3$-3s│%4$-3s│%5$-3s|%6$-4s│%7$-4s|%8$-3s│%9$-4s│\n",
				            "Club", "GP", ANSI_GREEN+"W "+ANSI_RESET, ANSI_YELLOW+"D "+ANSI_RESET, ANSI_RED+"L "+ANSI_RESET, ANSI_GREEN+"GF "+ANSI_RESET, ANSI_RED+"GA "+ANSI_RESET, "GD", ANSI_BLUE+"Pts"+ANSI_RESET);

        for (int i=0; i<myTeams.length; i++){
            System.out.println("├────────────────────────────────────────┼──┼──┼──┼──┼───┼───┼───┼───┤");
            System.out.format("|%1$-40s|%2$-2d|%3$-2d|%4$-2d|%5$-2d|%6$-3d|%7$-3d|%8$+-3d|%9$-3d|\n", myTeams[i].getName(), myTeams[i].getWins()+myTeams[i].getDraws()+myTeams[i].getLosses(), myTeams[i].getWins(), myTeams[i].getDraws(), myTeams[i].getLosses(), myTeams[i].getGoalsFor(), myTeams[i].getGoalsAgainst(), myTeams[i].getGoalsDifference(), myTeams[i].getPoints());
        }
        System.out.println("└────────────────────────────────────────┴──┴──┴──┴──┴───┴───┴───┴───┘\n\n\n");

        System.out.println("                          ***** PROMOTIONS *****                            ");
        System.out.println("┌────────────────────────────────────────┬──┬──┬──┬──┬───┬───┬───┬───┐");
		System.out.format("│%1$-40s│%2$-2s│%3$-3s│%4$-3s│%5$-3s|%6$-4s│%7$-4s|%8$-3s│%9$-4s│\n",
				            "Club", "GP", ANSI_GREEN+"W "+ANSI_RESET, ANSI_YELLOW+"D "+ANSI_RESET, ANSI_RED+"L "+ANSI_RESET, ANSI_GREEN+"GF "+ANSI_RESET, ANSI_RED+"GA "+ANSI_RESET, "GD", ANSI_BLUE+"Pts"+ANSI_RESET);

        for (int i=0; i<4; i++){
            System.out.println("├────────────────────────────────────────┼──┼──┼──┼──┼───┼───┼───┼───┤");
            System.out.format("|%1$-40s|%2$-2d|%3$-2d|%4$-2d|%5$-2d|%6$-3d|%7$-3d|%8$+-3d|%9$-3d|\n", myTeams[i].getName(), myTeams[i].getWins()+myTeams[i].getDraws()+myTeams[i].getLosses(), myTeams[i].getWins(), myTeams[i].getDraws(), myTeams[i].getLosses(), myTeams[i].getGoalsFor(), myTeams[i].getGoalsAgainst(), myTeams[i].getGoalsDifference(), myTeams[i].getPoints());
        }
        System.out.println("└────────────────────────────────────────┴──┴──┴──┴──┴───┴───┴───┴───┘\n\n\n");

        System.out.println("                          ***** RELEGATIONS *****                            ");
        System.out.println("┌────────────────────────────────────────┬──┬──┬──┬──┬───┬───┬───┬───┐");
		System.out.format("│%1$-40s│%2$-2s│%3$-3s│%4$-3s│%5$-3s|%6$-4s│%7$-4s|%8$-3s│%9$-4s│\n",
				            "Club", "GP", ANSI_GREEN+"W "+ANSI_RESET, ANSI_YELLOW+"D "+ANSI_RESET, ANSI_RED+"L "+ANSI_RESET, ANSI_GREEN+"GF "+ANSI_RESET, ANSI_RED+"GA "+ANSI_RESET, "GD", ANSI_BLUE+"Pts"+ANSI_RESET);

        for (int i=18; i<myTeams.length; i++){
            System.out.println("├────────────────────────────────────────┼──┼──┼──┼──┼───┼───┼───┼───┤");
            System.out.format("|%1$-40s|%2$-2d|%3$-2d|%4$-2d|%5$-2d|%6$-3d|%7$-3d|%8$+-3d|%9$-3d|\n", myTeams[i].getName(), myTeams[i].getWins()+myTeams[i].getDraws()+myTeams[i].getLosses(), myTeams[i].getWins(), myTeams[i].getDraws(), myTeams[i].getLosses(), myTeams[i].getGoalsFor(), myTeams[i].getGoalsAgainst(), myTeams[i].getGoalsDifference(), myTeams[i].getPoints());
        }
        System.out.println("└────────────────────────────────────────┴──┴──┴──┴──┴───┴───┴───┴───┘\n\n\n");
    }

    /**
     * Method which simulates all of the games in the league and displays the league table at the end of the season
     */
    public void simulateSession(){
        // Schedule matches for the season
        Team[] myTeams = teams.getValuesArray();
        Arrays.sort(myTeams, (Team t1, Team t2) -> t1.getBudget() - t2.getBudget());
        try{
            File logsDir = new File("logs");
            File logFile = new File(logsDir, "Execution Log - " + System.currentTimeMillis()+".txt");
            if(!logFile.exists()||!logFile.isFile()) logFile.createNewFile();
            FileWriter logOut = new FileWriter(logFile);
            for(Team team:myTeams)
                logOut.write(team.getName() + " : " + team.getBudget() + "€\n");
            
        shuffle(myTeams,logOut);

        this.leagueSetup(myTeams,logOut);

        // Should show the games played on screen
        for(int i=0; i<matchdays.size; i++){
            MatchDay auxMatchDay = (MatchDay) matchdays.get(i).data;
            auxMatchDay.playMatchDay();
            try{
                Thread.sleep(500);
            } catch(InterruptedException ex){
                
            }
        }

        printStandings(); // When the league is finished it prints the standings
    }
}

