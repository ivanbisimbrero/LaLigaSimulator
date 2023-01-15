package LaLiga;

import structures.*;

public class MatchDay {
	
	final String[] WEEK_DAYS = {"Friday", "Saturday", "Sunday", "Thursday"};
	int leg;
	int matchDayNumber;
	PriorityQueue matches;
	Team restingTeam;
	SingleLinkedList matchesPlayed;
	
	public MatchDay(int leg, int matchDayNumber) {
		this.leg = leg;
		this.matchDayNumber = matchDayNumber;
		this.matches = new PriorityQueue(this.WEEK_DAYS.length);
		this.restingTeam = null;
		this.matchesPlayed = new SingleLinkedList();
	}
	
	public boolean addMatch(Match match) {
		boolean success = !this.isTeamPlaying(match.getHomeTeam()) && !this.isTeamPlaying(match.getAwayTeam()) 
		&& match.getHomeTeam() != this.restingTeam && match.getAwayTeam() != this.restingTeam;
		if(success) {
			matches.push(match,setDayOfMatch(match));
		}
		return success;
	}

	public int getLeg() {
		return leg;
	}
	
	public int getMatchDayNumber() {
		return matchDayNumber;
	}
	
	public PriorityQueue getMatches() {
		return matches;
	}
	
	public int getNumberOfMatches() {
		return matches.size();
	}
	
	public boolean isTeamPlaying(Team team) {
        boolean isPlaying = false;
        PriorityQueue aux = new PriorityQueue(this.WEEK_DAYS.length);
        while (!this.matches.isEmpty()) {
            Match match = (Match) matches.poll();
            isPlaying = isPlaying || match.getAwayTeam() == team || match.getHomeTeam() == team;
            int prio = (match.getDay().equals(this.WEEK_DAYS[0])) ? 1 : 
                       (match.getDay().equals(this.WEEK_DAYS[1])) ? 2 : 
                       (match.getDay().equals(this.WEEK_DAYS[2])) ? 3 : 0;
            aux.push(match, prio);
        }
        this.matches = aux;
        return isPlaying;
	}
	
	public void playMatchDay() {
		for (int i = 0; i < this.matches.size(); i++){
			Match playingMatch = (Match)(this.matches.poll()); 
			playingMatch.simulate();
			matchesPlayed.insertLast(playingMatch);
			//System.out.println(((Match)(this.matches.get(i).data)));
		}
	}
	
	private int setDayOfMatch(Match match) {
        int day = (match.getAwayTeam().hasPlayedOnSunday() || match.getHomeTeam().hasPlayedOnSunday()) ? 
                    (int)(Math.random()*3) : 
                    (int)(Math.random()*4);
        match.setDay(this.WEEK_DAYS[day]);
        if (match.getDay().equals(this.WEEK_DAYS[2])){
            match.getAwayTeam().setHasPlayedOnSunday(true);
            match.getHomeTeam().setHasPlayedOnSunday(true);
        }
        
        return (day + 1) % 4;
    }
	
	public void setRestingTeam(Team restingTeam) {
		this.restingTeam = restingTeam;
	}

	public MatchDay getOppositeMatchDay(int leg, int MatchDayNumber) {
        
        PriorityQueue oldMatches = new PriorityQueue(MatchDay.WEEK_DAYS.length);
        PriorityQueue newMatches = new PriorityQueue(MatchDay.WEEK_DAYS.length);
        while (!this.matches.isEmpty()){
            Match match = (Match)this.matches.poll();
            int prio = (match.getDay().equals(MatchDay.WEEK_DAYS[0])) ? 1 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[1])) ? 2 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[2])) ? 3 : 0;
            oldMatches.push(match, prio);
            Match newMatch = new Match(match.getAwayTeam(), match.getHomeTeam());
            newMatch.setDay(match.getDay());
            newMatches.push(newMatch , prio);
        }
        
        this.matches = oldMatches;
        return new MatchDay(leg, MatchDayNumber, newMatches, restingTeam);
        
    }

}
