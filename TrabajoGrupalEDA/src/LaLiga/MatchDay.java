package LaLiga;

import structures.*;

public class MatchDay {
	
	final String[] WEEK_DAYS = {"Friday", "Saturday", "Sunday", "Thursday"};
	int leg;
	int matchDayNumber;
	SingleLinkedList matches;
	//Team restingTeam;
	
	public MatchDay(int leg, int matchDayNumber) {
		this.leg = leg;
		this.matchDayNumber = matchDayNumber;
		matches = new SingleLinkedList();
	}
	
	//TODO: addMatch cuando tenga las estructuras correspondientes
	public boolean addMatch(/*Match match*/) {
		return true; //No alarmarse lo cambiare perros
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
	
	public boolean isTeamPlaying(/*Team team*/) {
		//TODO: funcion para comprobar si el equipo esta jugando
		return true; //No alarmarse lo cambiare perros
	}
	
	public void playMatchDay() {
		//TODO: funcion para jugar un dia
	}
	
	private void setDayOfMatch(/*Match match*/) {
		//TODO: funcion que establece el dia del encuentro
	}
	
	public void setRestingTeam(/*Team restingTeam*/) {
		//TODO: funcion para setear el equipo visitante
	}

}
