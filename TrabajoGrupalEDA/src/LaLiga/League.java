package LaLiga;

import java.util.Random;
import helpers.SortingLibrary;
import structures.*;

public class League {

    private int MAX_TEAMS = 21;
    private static int PLAYER_AGE = 0;
    private static int PLAYER_COST = 1;
    private static int PLAYER_NAME = 2;
    private static int PLAYER_POSITION = 3;
    private static int PLAYER_TEAM = 4;
    private static int TEAM_NAME = 5;
    private static int TEAM_SHORT = 6;

    private String name; // name of the league
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK =  "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE =  "\u001B[34m";

    SingleLinkedList matchdays; // matchdays of the whole league
    
    SortingLibrary<Team> mySorter; // Library to sort teams
    
    HashMap teams; // hashmap to save the teams 

    /**
     * Default constructor for the league
     */
    public League(){
        this.matchdays = new SingleLinkedList();
        this.mySorter = new SortingLibrary<Team>();
        this.teams = new HashMap(MAX_TEAMS);
        this.name = "";
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
    public SingleLinkedList getMatches(Team[] myTeams, int leg){
        myTeams = shuffle(myTeams); // Organise randomly the order of the teams
        Random rand = new Random(); // Index to get a random team
        SingleLinkedList auxMatches = new SingleLinkedList();
        if(leg == 0){
            System.out.println("Shuffling first leg with Bogosort...\n");
            System.out.println("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠿⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⠀⠀⠀⠀⠀⡀⠠⠤⠒⢂⣉⣉⣉⣑⣒⣒⠒⠒⠒⠒⠒⠒⠒⠀⠀⠐⠒⠚⠻⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⠀⡠⠔⠉⣀⠔⠒⠉⣀⣀⠀⠀⠀⣀⡀⠈⠉⠑⠒⠒⠒⠒⠒⠈⠉⠉⠉⠁⠂⠀⠈⠙⢿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠔⠁⠠⠖⠡⠔⠊⠀⠀⠀⠀⠀⠀⠀⠐⡄⠀⠀⠀⠀⠀⠀⡄⠀⠀⠀⠀⠉⠲⢄⠀⠀⠀⠈⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠊⠀⢀⣀⣤⣤⣤⣤⣀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠜⠀⠀⠀⠀⣀⡀⠀⠈⠃⠀⠀⠀⠸⣿⣿⣿⣿\n⣿⣿⣿⣿⡿⠥⠐⠂⠀⠀⠀⠀⡄⠀⠰⢺⣿⣿⣿⣿⣿⣟⠀⠈⠐⢤⠀⠀⠀⠀⠀⠀⢀⣠⣶⣾⣯⠀⠀⠉⠂⠀⠠⠤⢄⣀⠙⢿⣿⣿\n⣿⡿⠋⠡⠐⠈⣉⠭⠤⠤⢄⡀⠈⠀⠈⠁⠉⠁⡠⠀⠀⠀⠉⠐⠠⠔⠀⠀⠀⠀⠀⠲⣿⠿⠛⠛⠓⠒⠂⠀⠀⠀⠀⠀⠀⠠⡉⢢⠙⣿\n⣿⠀⢀⠁⠀⠊⠀⠀⠀⠀⠀⠈⠁⠒⠂⠀⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⢀⣀⡠⠔⠒⠒⠂⠀⠈⠀⡇⣿\n⣿⠀⢸⠀⠀⠀⢀⣀⡠⠋⠓⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠈⠢⠤⡀⠀⠀⠀⠀⠀⠀⢠⠀⠀⠀⡠⠀⡇⣿\n⣿⡀⠘⠀⠀⠀⠀⠀⠘⡄⠀⠀⠀⠈⠑⡦⢄⣀⠀⠀⠐⠒⠁⢸⠀⠀⠠⠒⠄⠀⠀⠀⠀⠀⢀⠇⠀⣀⡀⠀⠀⢀⢾⡆⠀⠈⡀⠎⣸⣿\n⣿⣿⣄⡈⠢⠀⠀⠀⠀⠘⣶⣄⡀⠀⠀⡇⠀⠀⠈⠉⠒⠢⡤⣀⡀⠀⠀⠀⠀⠀⠐⠦⠤⠒⠁⠀⠀⠀⠀⣀⢴⠁⠀⢷⠀⠀⠀⢰⣿⣿\n⣿⣿⣿⣿⣇⠂⠀⠀⠀⠀⠈⢂⠀⠈⠹⡧⣀⠀⠀⠀⠀⠀⡇⠀⠀⠉⠉⠉⢱⠒⠒⠒⠒⢖⠒⠒⠂⠙⠏⠀⠘⡀⠀⢸⠀⠀⠀⣿⣿⣿\n⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠑⠄⠰⠀⠀⠁⠐⠲⣤⣴⣄⡀⠀⠀⠀⠀⢸⠀⠀⠀⠀⢸⠀⠀⠀⠀⢠⠀⣠⣷⣶⣿⠀⠀⢰⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠁⢀⠀⠀⠀⠀⠀⡙⠋⠙⠓⠲⢤⣤⣷⣤⣤⣤⣤⣾⣦⣤⣤⣶⣿⣿⣿⣿⡟⢹⠀⠀⢸⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠑⠀⢄⠀⡰⠁⠀⠀⠀⠀⠀⠈⠉⠁⠈⠉⠻⠋⠉⠛⢛⠉⠉⢹⠁⢀⢇⠎⠀⠀⢸⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣀⠈⠢⢄⡉⠂⠄⡀⠀⠈⠒⠢⠄⠀⢀⣀⣀⣰⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⢀⣎⠀⠼⠊⠀⠀⠀⠘⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⡀⠉⠢⢄⡈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⢀⠀⠀⠀⠀⠀⢻⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣀⡈⠑⠢⢄⡀⠈⠑⠒⠤⠄⣀⣀⠀⠉⠉⠉⠉⠀⠀⠀⣀⡀⠤⠂⠁⠀⢀⠆⠀⠀⢸⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣄⡀⠁⠉⠒⠂⠤⠤⣀⣀⣉⡉⠉⠉⠉⠉⢀⣀⣀⡠⠤⠒⠈⠀⠀⠀⠀⣸⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣶⣶⣤⣤⣤⣤⣀⣀⣤⣤⣤⣶⣾⣿⣿⣿⣿⣿\n");
            for(int i=0; i<(myTeams.length-1); i++){
                MatchDay auxMatchDay = new MatchDay(0, i+1);
                for(int j=0; j<myTeams.length; j++){
                    for(int k=j+1; k<myTeams.length; k++){
                        Match auxMatch = new Match(myTeams[j], myTeams[k]);
                        auxMatchDay.addMatch(auxMatch);
                        auxMatchDay.setDayOfMatch(auxMatch);
                        auxMatchDay.setRestingTeam(myTeams[rand.nextInt(myTeams.length)]);
                        auxMatches.insertLast(auxMatchDay);
                    }
                }
            }
        }else 
        if(leg == 1){
            System.out.println("Shuffling second leg with Bogosort...\n");
            System.out.println("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠿⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⠀⠀⠀⠀⠀⡀⠠⠤⠒⢂⣉⣉⣉⣑⣒⣒⠒⠒⠒⠒⠒⠒⠒⠀⠀⠐⠒⠚⠻⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⠀⡠⠔⠉⣀⠔⠒⠉⣀⣀⠀⠀⠀⣀⡀⠈⠉⠑⠒⠒⠒⠒⠒⠈⠉⠉⠉⠁⠂⠀⠈⠙⢿⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠔⠁⠠⠖⠡⠔⠊⠀⠀⠀⠀⠀⠀⠀⠐⡄⠀⠀⠀⠀⠀⠀⡄⠀⠀⠀⠀⠉⠲⢄⠀⠀⠀⠈⣿⣿⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠊⠀⢀⣀⣤⣤⣤⣤⣀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠜⠀⠀⠀⠀⣀⡀⠀⠈⠃⠀⠀⠀⠸⣿⣿⣿⣿\n⣿⣿⣿⣿⡿⠥⠐⠂⠀⠀⠀⠀⡄⠀⠰⢺⣿⣿⣿⣿⣿⣟⠀⠈⠐⢤⠀⠀⠀⠀⠀⠀⢀⣠⣶⣾⣯⠀⠀⠉⠂⠀⠠⠤⢄⣀⠙⢿⣿⣿\n⣿⡿⠋⠡⠐⠈⣉⠭⠤⠤⢄⡀⠈⠀⠈⠁⠉⠁⡠⠀⠀⠀⠉⠐⠠⠔⠀⠀⠀⠀⠀⠲⣿⠿⠛⠛⠓⠒⠂⠀⠀⠀⠀⠀⠀⠠⡉⢢⠙⣿\n⣿⠀⢀⠁⠀⠊⠀⠀⠀⠀⠀⠈⠁⠒⠂⠀⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⢀⣀⡠⠔⠒⠒⠂⠀⠈⠀⡇⣿\n⣿⠀⢸⠀⠀⠀⢀⣀⡠⠋⠓⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠈⠢⠤⡀⠀⠀⠀⠀⠀⠀⢠⠀⠀⠀⡠⠀⡇⣿\n⣿⡀⠘⠀⠀⠀⠀⠀⠘⡄⠀⠀⠀⠈⠑⡦⢄⣀⠀⠀⠐⠒⠁⢸⠀⠀⠠⠒⠄⠀⠀⠀⠀⠀⢀⠇⠀⣀⡀⠀⠀⢀⢾⡆⠀⠈⡀⠎⣸⣿\n⣿⣿⣄⡈⠢⠀⠀⠀⠀⠘⣶⣄⡀⠀⠀⡇⠀⠀⠈⠉⠒⠢⡤⣀⡀⠀⠀⠀⠀⠀⠐⠦⠤⠒⠁⠀⠀⠀⠀⣀⢴⠁⠀⢷⠀⠀⠀⢰⣿⣿\n⣿⣿⣿⣿⣇⠂⠀⠀⠀⠀⠈⢂⠀⠈⠹⡧⣀⠀⠀⠀⠀⠀⡇⠀⠀⠉⠉⠉⢱⠒⠒⠒⠒⢖⠒⠒⠂⠙⠏⠀⠘⡀⠀⢸⠀⠀⠀⣿⣿⣿\n⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠑⠄⠰⠀⠀⠁⠐⠲⣤⣴⣄⡀⠀⠀⠀⠀⢸⠀⠀⠀⠀⢸⠀⠀⠀⠀⢠⠀⣠⣷⣶⣿⠀⠀⢰⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠁⢀⠀⠀⠀⠀⠀⡙⠋⠙⠓⠲⢤⣤⣷⣤⣤⣤⣤⣾⣦⣤⣤⣶⣿⣿⣿⣿⡟⢹⠀⠀⢸⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠑⠀⢄⠀⡰⠁⠀⠀⠀⠀⠀⠈⠉⠁⠈⠉⠻⠋⠉⠛⢛⠉⠉⢹⠁⢀⢇⠎⠀⠀⢸⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣀⠈⠢⢄⡉⠂⠄⡀⠀⠈⠒⠢⠄⠀⢀⣀⣀⣰⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⢀⣎⠀⠼⠊⠀⠀⠀⠘⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⡀⠉⠢⢄⡈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⢀⠀⠀⠀⠀⠀⢻⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣀⡈⠑⠢⢄⡀⠈⠑⠒⠤⠄⣀⣀⠀⠉⠉⠉⠉⠀⠀⠀⣀⡀⠤⠂⠁⠀⢀⠆⠀⠀⢸⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣄⡀⠁⠉⠒⠂⠤⠤⣀⣀⣉⡉⠉⠉⠉⠉⢀⣀⣀⡠⠤⠒⠈⠀⠀⠀⠀⣸⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⣿\n⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣶⣶⣤⣤⣤⣤⣀⣀⣤⣤⣤⣶⣾⣿⣿⣿⣿⣿\n");
            for(int i=0; i<(myTeams.length-1); i++){
                MatchDay auxMatchDay = new MatchDay(1, i+1);
                for(int j=myTeams.length-1; j>0; j--){
                    for(int k=j-1; k>=0; k--){
                        Match auxMatch = new Match(myTeams[j], myTeams[k]);
                        auxMatchDay.addMatch(auxMatch);
                        auxMatchDay.setDayOfMatch(auxMatch);
                        auxMatchDay.setRestingTeam(myTeams[rand.nextInt(myTeams.length)]);
                        auxMatches.insertLast(auxMatchDay);   
                    }
                }
                auxMatches.insertLast(auxMatchDay);
            }
        }else{
            return null; // Non valid input
        }
        return auxMatches;
    }

    /**
     * Method which randomly shuffles a team (one of the steps in the bogosort algorithm)
     * @param myTeams the teams we want to shuffle
     * @return the array of teams shuffled
     */
    public Team[] shuffle(Team[] myTeams){
        Random rand = new Random();
        if(myTeams!=null){
            for(int i=0; i<myTeams.length; i++){
                Team aux = myTeams[i];
                myTeams[i] = myTeams[rand.nextInt(myTeams.length)];
                myTeams[rand.nextInt(myTeams.length)] = aux;
            }   
        }
        return myTeams;
    }
    /**
     * Method which loads all teams and players from a CSV file
     */
    public void loadTeamsAndPlayers(){
        for(int i=0; i<MAX_TEAMS;  i++){

        }
    }

    /**
     * Method which creates the matchdays 
     * @param myTeams array of teams 
     * @param first list of the games 
     */
    public void matchDaysSetup(Team[] myTeams, SingleLinkedList first){
        for(int i=0; i<first.size; i++){
            matchdays.insertLast(first.get(i).data);
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
        SingleLinkedList firstFixtures, secondFixtures; // Lists of the first leg and the second leg
        Team[] myTeams = teams.getValuesArray();
        
        firstFixtures = getMatches(myTeams, 0); // Generate the matches in the first leg
        secondFixtures = getMatches(myTeams, 1); // Generate the matches in the second leg
        
        matchDaysSetup(myTeams, firstFixtures);
        matchDaysSetup(myTeams, secondFixtures);

        // Should show the games played on screen
        for(int i=0; i<matchdays.size; i++){
            MatchDay auxMatchDay = (MatchDay) matchdays.get(i).data;
            auxMatchDay.playMatchDay();
        }

        printStandings(); // When the league is finished it prints the standings
    }
}

