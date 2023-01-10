package LaLiga;

import structures.*;

public class MatchDay {
	
	final String[] WEEK_DAYS = {"Friday", "Saturday", "Sunday", "Thursday"};
	int leg;
	int matchDayNumber;
	SingleLinkedList matches;
	Team restingTeam;
	
	public MatchDay(int leg, int matchDayNumber) {
		this.leg = leg;
		this.matchDayNumber = matchDayNumber;
		this.matches = new SingleLinkedList();
		this.restingTeam = null;
	}
	
	//TODO: addMatch para añadir un partido
	public boolean addMatch(Match match) {
		boolean success = !this.isTeamPlaying(match.getHomeTeam()) && !this.isTeamPlaying(match.getAwayTeam()) 
		&& match.getHomeTeam() != this.restingTeam && match.getAwayTeam() != this.restingTeam;
		if(success) {
			matches.insertHead(match);
		}
		return success;
	}

	public int getLeg() {
		return leg;
	}
	
	public int getMatchDayNumber() {
		return matchDayNumber;
	}
	
	public SingleLinkedList getMatches() {
		return matches;
	}
	
	public int getNumberOfMatches() {
		return matches.size;
	}
	
	public boolean isTeamPlaying(Team team) {
		return team.isActive();
	}
	
	public void playMatchDay() {
		for (int i = 0; i < this.matches.size; i++){
			((Match)(this.matches.get(i).data)).simulate();
			//System.out.println(((Match)(this.matches.get(i).data)));
		}
	}
	
	private void setDayOfMatch(Match match) {
		if(match.getHomeTeam().hasPlayedOnSunday() || match.getAwayTeam().hasPlayedOnSunday()){
			match.setDay(WEEK_DAYS[(int) Math.rand()*3]);
		} else {
			match.setDay(WEEK_DAYS[(int) Math.rand()*4]);
		}
		if(match.getDay().equals(WEEK_DAYS[2])){
			match.getAwayTeam().setHasPlayedOnSunday(true);
			match.getTeam().setHasPlayedOnSunday(true);
		}
	}
	
	public void setRestingTeam(Team restingTeam) {
		this.restingTeam = restingTeam;
	}

}
