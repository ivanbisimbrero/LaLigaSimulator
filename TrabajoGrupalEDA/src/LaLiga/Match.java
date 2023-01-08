package LaLiga;

import java.util.Scanner;

public class Match {
	Scanner sc= new Scanner(System.in);
	private final int HIGH_DIF=20;
	private final int LOW_DIF=5;
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
	
	public void simulate(){ //const
		System.out.println("PARTIDO: "+homeTeam.getName()+" vs "+awayTeam.getName()+": "+homeScore
				+"-"+awayScore+"\n");
		homeTeam.addGoalsFor(homeScore);
		homeTeam.addGoalsAgainst(awayScore);
		awayTeam.addGoalsFor(awayScore);
		awayTeam.addGoalsAgainst(homeScore);
		//PUNTUACIÓN
	}
}
