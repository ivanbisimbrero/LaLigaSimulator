package LaLiga;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import helpers.SortingLibrary;
import structures.*;

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

    /**
     * Method which gets the matches of a leg 
     * @param myTeams arrray of teams
     * @param leg number of the leg
     * @return List with all the games
     */
    public SingleLinkedList getMatches(Team[] myTeams){
        SingleLinkedList matches = new SingleLinkedList();
            for (int i = 0; i < myTeams.length; ++i) {
                Team homeTeam = myTeams[i];
                for (int j = i + 1; j < myTeams.length; ++j) {
                    matches.insertLast(new Match(homeTeam, myTeams[j]));
                }
            }
        return matches;
    }

    /**
     * Method which randomly shuffles a team array
     * @param myTeams the teams we want to shuffle
     */
    public void shuffle(Team[] myTeams){
        if(myTeams!=null){
            Random rand = new Random();
            for(int i=0; i<myTeams.length; i++){
                int swap = rand.nextInt(myTeams.length);
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
     * Method which creates the matchdays 
     * @param myTeams array of teams 
     * @param first list of the games 
     */
    public void matchDaysSetup(Team[] myTeams, SingleLinkedList matches){
        SingleLinkedList matchdaysSetup = new SingleLinkedList();
        SingleLinkedList secondLegMatchdaysSetup = new SingleLinkedList();
        int indexOfResting = (myTeams.length%2== 0) ? -1 : myTeams.length - 1;
        
        int numberOfMatchDays = myTeams.length;
        if (myTeams.length%2 == 0) --numberOfMatchDays;
        
        for (int i = 0; i < numberOfMatchDays; ++i){
            Matchday matchday = new Matchday(0, i+1);
            if (indexOfResting != -1) matchday.setRestingTeam(myTeams[indexOfResting--]);
            for (int j = 0; j < matches.size;j++){
                Match match = (Match)matches.get(j).data;
                if(matchday.addMatch(match)){
                    matches.remove(match);
                    --j;
                }
            }
            matchdaysSetup.insertLast(matchday);
        }
        System.out.println("Matches size = " + matches.size);
        
        /*
        //Volcado de full copies de la primera tanda al setup de la segunda
        for (int i = 0; i < matchdaysSetup.size; ++i){
            Matchday secondLegMatchday = ((Matchday)matchdaysSetup.get(i)).getOppositeMatchday(1,((Matchday)matchdaysSetup.get(i)).getMatchdayNumber()+numberOfMatchDays);
            secondLegMatchdaysSetup.insertLast(secondLegMatchday);
        }
        
        for(int i = 0; i < secondLegMatchdaysSetup.size; i++){
            matchdaysSetup.insertLast(secondLegMatchdaysSetup.get(i));
        }
        */
        
        //limpiamos la lista de la tanda y la rellenamos con full copies de la lista completa (actualmente contiene la tanda real
        
        /*
        int numberOfMatchdaysPerLeg = (myTeams.length % 2 == 1) ? myTeams.length : myTeams.length - 1; //myTeams.length - (myTeams.length + 1) %2
        int leg = 1;
        SingleLinkedList activeMatchList = secondLegMatches;
        for (int i = numberOfMatchdaysPerLeg; i > 0; --i) { // We are creating them backawards as inserting head in list is quicker than inserting last
            Matchday matchday = new Matchday(leg, i);
            matchdays.insertHead(matchday);
            matchday.setRestingTeam((myTeams.length % 2 == 0) ? null : myTeams[i - 1]);
            for (int j = 0; j < activeMatchList.size; ++j) {
                Match nextMatch = (Match) activeMatchList.get(j);
                if (matchday.addMatch(nextMatch)) {
                    activeMatchList.remove(nextMatch);
                    --j; //Because we are removing an element from the list, we have to correct the j to the previous element
                }
            }

            if (i == 1 && leg == 1) {
                i = numberOfMatchdaysPerLeg + 1;
                leg = 0;
                activeMatchList = firstLegMatches;
            }
        }*/
        
        this.matchdays = matchdaysSetup;
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
        Team[] myTeams = teams.getValuesArray();
        shuffle(myTeams);
        SingleLinkedList firstFixtures =getMatches(myTeams);// Generate the matches in the first leg

        
        matchDaysSetup(myTeams, firstFixtures);

        // Should show the games played on screen
        for(int i=0; i<matchdays.size; i++){
            MatchDay auxMatchDay = (MatchDay) matchdays.get(i).data;
            auxMatchDay.playMatchDay();
        }

        printStandings(); // When the league is finished it prints the standings
    }
}

