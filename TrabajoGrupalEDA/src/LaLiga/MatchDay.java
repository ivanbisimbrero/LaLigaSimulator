package LaLiga;

import structures.*;

public class MatchDay {
	
	static final String[] WEEK_DAYS = {"Friday", "Saturday", "Sunday", "Thursday"};
	
	private int matchdayNumber;
    private PriorityQueue scheduledMatches;
    private final SingleLinkedList matches;
    private Team restingTeam;

	private MatchDay(int matchdayNumber, PriorityQueue scheduledMatches,SingleLinkedList matches, Team restingTeam) {
        this.matchdayNumber = matchdayNumber;
        this.scheduledMatches = scheduledMatches;
        this.matches = matches;
        this.restingTeam = restingTeam;
    }
    
    public MatchDay() {
        this.matchdayNumber = 0;
        this.scheduledMatches = new PriorityQueue(WEEK_DAYS.length);
        this.matches = new SingleLinkedList();
        this.restingTeam = null;
    }
    
    public MatchDay(int matchdayNumber) {
        this.matchdayNumber = matchdayNumber;
        this.scheduledMatches = new PriorityQueue(WEEK_DAYS.length);
        this.matches = new SingleLinkedList();
        this.restingTeam = null;
    }
	
	public boolean addMatch(Match match) {
		boolean added = !this.isTeamPlaying(match.getHomeTeam()) && !this.isTeamPlaying(match.getAwayTeam()) // Teams are not playing yet
                && match.getHomeTeam() != this.restingTeam && match.getAwayTeam() != this.restingTeam;// Teams are not the resting team
        if (added) {
            this.matches.insertHead(match);
        }
        return added;
	}
	
	public int getMatchDayNumber() {
		return matchdayNumber;
	}
	
	public PriorityQueue getScheduledMatches() {
        return scheduledMatches;
    }
	
	public int getNumberOfMatches() {
		int numberOfMatches = 0;
        PriorityQueue aux = new PriorityQueue(WEEK_DAYS.length);
        while (!this.scheduledMatches.isEmpty()) {
            Match match = (Match) scheduledMatches.poll();
            numberOfMatches++;
            int prio = (match.getDay().equals(MatchDay.WEEK_DAYS[0])) ? 1 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[1])) ? 2 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[2])) ? 3 : 0;
            aux.push(match, prio);
        }
        this.scheduledMatches = aux;
        return numberOfMatches + this.matches.size;
	}
	
	public boolean isTeamPlaying(Team team) {
        boolean isPlaying = false;
        PriorityQueue aux = new PriorityQueue(WEEK_DAYS.length);
        while (!this.scheduledMatches.isEmpty()) {
            Match match = (Match) scheduledMatches.poll();
            isPlaying = isPlaying || match.getAwayTeam() == team || match.getHomeTeam() == team;
            int prio = (match.getDay().equals(MatchDay.WEEK_DAYS[0])) ? 1 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[1])) ? 2 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[2])) ? 3 : 0;
            aux.push(match, prio);
        }
        this.scheduledMatches = aux;
        return isPlaying || this.matches.contains(team);
	}
	
	public void playMatchDay() {
		System.out.println("Playing matchday " + matchdayNumber);
        String day = MatchDay.WEEK_DAYS[3];
        System.out.println(day);
        while (!scheduledMatches.isEmpty()) {
            Match playingMatch = (Match) scheduledMatches.poll();
            if (!playingMatch.getDay().equals(day)){
                day = playingMatch.getDay();
                System.out.println(day);
            }
            playingMatch.simulate();
            System.out.println(playingMatch);
        }
	}
	
	public void setDayOfMatches() {
        boolean hasThursday = false;
        boolean hasFriday = false;
        boolean hasSaturday = false;
        boolean hasSunday = false;
        Match match;
        while(!this.matches.isEmpty()){
            match = (Match) this.matches.remove(this.matches.get(0).data);
            int day;
            if(!hasThursday && !((match.getAwayTeam().hasPlayedOnSunday() || match.getHomeTeam().hasPlayedOnSunday()))){
                day = 3;
                hasThursday = true;
            }
            else if(!hasFriday){
                day = 0;
                hasFriday = true;
            }
            else if(!hasSaturday){
                day = 1;
                hasSaturday = true;
            }
            else if(!hasSunday){
                day = 2;
                hasSunday = true;
            }
            else day = (match.getAwayTeam().hasPlayedOnSunday() || match.getHomeTeam().hasPlayedOnSunday()) ? 
                        (int)(Math.random()*3) : (int)(Math.random()*4);
            match.setDay(MatchDay.WEEK_DAYS[day]);
            if (match.getDay().equals(MatchDay.WEEK_DAYS[2])){
                match.getAwayTeam().setHasPlayedOnSunday(true);
                match.getHomeTeam().setHasPlayedOnSunday(true);
            } else {
                match.getAwayTeam().setHasPlayedOnSunday(false);
                match.getHomeTeam().setHasPlayedOnSunday(false);
            }
            this.scheduledMatches.push(match, (day + 1) % 4);
        }
    }
	
	public void setRestingTeam(Team restingTeam) {
		this.restingTeam = restingTeam;
	}

	public MatchDay cloneOpposite(int matchdayNumber) {
        
        PriorityQueue oldMatches = new PriorityQueue(MatchDay.WEEK_DAYS.length);
        PriorityQueue newMatches = new PriorityQueue(MatchDay.WEEK_DAYS.length);
        while (!this.scheduledMatches.isEmpty()){
            Match match = (Match)this.scheduledMatches.poll();
            int prio = (match.getDay().equals(MatchDay.WEEK_DAYS[0])) ? 1 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[1])) ? 2 : 
                       (match.getDay().equals(MatchDay.WEEK_DAYS[2])) ? 3 : 0;
            oldMatches.push(match, prio);
            Match newMatch = new Match(match.getAwayTeam(), match.getHomeTeam());
            newMatch.setDay(match.getDay());
            newMatches.push(newMatch , prio);
        }
        
        SingleLinkedList newMatchesList = new SingleLinkedList();
        for (int i = 0; i < this.matches.size;++i){
            newMatchesList.insertHead(new Match(
                    ((Match)this.matches.get(i).data).getAwayTeam(), ((Match)this.matches.get(i).data).getHomeTeam())
            );
        }
        this.scheduledMatches = oldMatches;
        return new MatchDay(matchdayNumber, newMatches, newMatchesList, restingTeam);
        
    }

}
