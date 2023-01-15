package LaLiga;

import java.util.Random;

public class Match {
    private static final int HIGH_DIFF = 20000000;
    private static final int LOW_DIFF = 5000000;
    private static final Random RAND = new Random(System.currentTimeMillis());

    private final Team homeTeam;
    private final Team awayTeam;
    private int homeScore;
    private int awayScore;
    private String day;
    

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = -1;
        this.awayScore = -1;
        this.day = "undefined";
        this.homeTeam.addMatchHome();
        this.awayTeam.addMatchAway();
    }

    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    
    private void getResult(int[] probabilities){
        int results = Match.RAND.nextInt(100); // 0-99 
        if (results < probabilities[0]) { // home team wins, away team routed
                homeScore = (Match.RAND.nextInt(5) + 3);
                int aux = Match.RAND.nextInt(5);
                awayScore = (homeScore - aux < 0) ? 0 : (homeScore - aux < 3) ? homeScore - 3 : homeScore - aux;
            } else if (results < (probabilities[0]+probabilities[1])) { // home team wins
                homeScore = Match.RAND.nextInt(6) + 2;
                int aux = Match.RAND.nextInt(2) + 1;// 1 or 2
                awayScore = homeScore - aux;
            } else if (results < (probabilities[0]+probabilities[1]+probabilities[2]) ) { // draw
                homeScore = Match.RAND.nextInt(8);
                awayScore = homeScore;
            } else if (results < (probabilities[0]+probabilities[1]+probabilities[2]+probabilities[3])) { // away team wins
                homeScore = Match.RAND.nextInt(6) + 2;
                int aux = Match.RAND.nextInt(2) + 1;// 1 or 2
                awayScore = homeScore - aux;
            } else { // away team wins, home team routed
                awayScore = (Match.RAND.nextInt(5) + 3);
                int aux = Match.RAND.nextInt(5);
                homeScore = (awayScore - aux < 0) ? 0 : (awayScore - aux < 3) ? awayScore - 3 : awayScore - aux;
            }
    }
	
    public void simulate() {
        int budgetDiff = homeTeam.getBudget() - awayTeam.getBudget(); // if home > away, diff > 0, else diff < 0
        int[] probabilities;
        // Changing from 0-99 to 1-100 (Easier to understand)
        if (budgetDiff > Match.HIGH_DIFF) { //High difference between budgets (>20m)
            probabilities = new int[]{15,50,20,10,5};
            //System.out.println("+HIGH DIFF");
        } else if (budgetDiff >= Match.LOW_DIFF) { // Medium difference between budgets(5m - 20m)
            probabilities = new int[]{10,40,30,15,5};
            //System.out.println("+MEDIUM DIFF");
        } else if (budgetDiff > 0) { // Little difference between budgets (<5m) 
            probabilities = new int[]{5,30,40,20,5};
            //System.out.println("+LOW DIFF");
        } else if (budgetDiff == 0){ // No difference between budgets
            probabilities = new int[]{5,20,50,20,5};
            //System.out.println("NO DIFF");
        } else if (budgetDiff > (-Match.LOW_DIFF)){
            probabilities = new int[]{5,20,40,20,5};
            //System.out.println("-LOW DIFF");
        } else if (budgetDiff >= (-Match.HIGH_DIFF)){
            probabilities = new int[]{5,15,30,40,10};
            //System.out.println("-MEDIUM DIFF");
        } else{
            //System.out.println("-HIGH DIFF");
            probabilities = new int[]{5,10,20,50,15};
        }
        this.getResult(probabilities);
        
        homeTeam.addGoalsAgainst(awayScore);
        homeTeam.addGoalsFor(homeScore);
        awayTeam.addGoalsAgainst(homeScore);
        awayTeam.addGoalsFor(awayScore);
        if (homeScore == awayScore){
            homeTeam.draw();
            awayTeam.draw();
        }
        else if (homeScore > awayScore){
            homeTeam.win();
            awayTeam.loss();
        }
        else {
            homeTeam.loss();
            awayTeam.win();
        }
    }

    @Override
    public String toString() {
        String toString = "";
        if(homeScore < 0 || awayScore < 0){
            toString = homeTeam.getShortName() + " -  - " + awayTeam.getShortName(); 
        } else {
            toString = homeTeam.getShortName() + " " + homeScore + " - " + awayScore + " " + awayTeam.getShortName();
        }
        
        return toString;
    }
    
    
}
