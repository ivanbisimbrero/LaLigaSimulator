package LaLiga;

import java.util.Random;

public class Match {
	private static final int HIGH_DIFF = 20000000;
    private static final int LOW_DIFF = 5000000;
    private static final Random RAND = new Random(System.currentTimeMillis());
	private int awayScore;
	private Team awayTeam;
	private String day;
	private int homeScore;
	private Team homeTeam;
	
	public Match(Team awayTeam, Team homeTeam) {
		this.awayTeam = awayTeam;
		this.homeTeam = homeTeam;
	}
	
	public int getAwayScore() {return awayScore;}
	
	public Team getAwayTeam() {return awayTeam;}
	
	public String getDay() {return day;}
	
	public int getHomeScore() {return homeScore;}
	
	public Team getHomeTeam() {return homeTeam;}
	
	public void setDay(String day) {
		this.day=day;
	}
	
	public void setScore(int homeScore,int awayScore) {
		this.homeScore=homeScore;
		this.awayScore=awayScore;
	}
	
	private void getResult(int[] probabilities){
        int results = Match.RAND.nextInt(100); // 0-99 
        if (results < probabilities[0]) { // home team wins, away team routed
                homeScore = (Match.RAND.nextInt(5) + 3);
                int aux = Match.RAND.nextInt(5);
                awayScore = (homeScore - aux < 0) ? 0 : (homeScore - aux < 3) ? homeScore - 3 : homeScore - aux;
            } else if (results < probabilities[1]) { // home team wins
                homeScore = Match.RAND.nextInt(6) + 2;
                int aux = Match.RAND.nextInt(2) + 1;// 1 or 2
                awayScore = homeScore - aux;
            } else if (results < probabilities[2]) { // draw
                homeScore = Match.RAND.nextInt(8);
                awayScore = homeScore;
            } else if (results < probabilities[3]) { // away team wins
                homeScore = Match.RAND.nextInt(6) + 2;
                int aux = Match.RAND.nextInt(2) + 1;// 1 or 2
                awayScore = homeScore - aux;
            } else { // away team wins, home team routed
                awayScore = (Match.RAND.nextInt(5) + 3);
                int aux = Match.RAND.nextInt(5);
                homeScore = (awayScore - aux < 0) ? 0 : (awayScore - aux < 3) ? awayScore - 3 : awayScore - aux;
            }
    }
    // +20|20|5|0
    public void simulate() {
        int budgetDiff = homeTeam.getBudget() - awayTeam.getBudget(); // if home > away, diff > 0, else diff < 0
        int[] probabilities;
        // Changing from 0-99 to 1-100 (Easier to understand)
        if (budgetDiff > Match.HIGH_DIFF) { //High difference between budgets (>20m)
            probabilities = new int[]{15,65,85,95,100};
            System.out.println("+HIGH DIFF");
        } else if (budgetDiff >= Match.LOW_DIFF) { // Medium difference between budgets(5m - 20m)
            probabilities = new int[]{10,50,80,95,100};
            System.out.println("+MEDIUM DIFF");
        } else if (budgetDiff > 0) { // Little difference between budgets (<5m) 
            probabilities = new int[]{5,35,75,95,100};
            System.out.println("+LOW DIFF");
        } else if (budgetDiff == 0){ // No difference between budgets
            probabilities = new int[]{5,25,75,95,100};
            System.out.println("NO DIFF");
        } else if (budgetDiff > (-Match.LOW_DIFF)){
            probabilities = new int[]{5,25,65,95,100};
            System.out.println("-LOW DIFF");
        } else if (budgetDiff >= (-Match.HIGH_DIFF)){
            probabilities = new int[]{5,20,50,90,100};
            System.out.println("-MEDIUM DIFF");
        } else{
            System.out.println("-HIGH DIFF");
            probabilities = new int[]{5,15,35,85,100};
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
