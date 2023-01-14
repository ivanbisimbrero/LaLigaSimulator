package LaLiga;

import java.util.Random;

public class Match {
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
		int goles;
		double []arrProb=new double[5];
		int difBudget=homeTeam.getBudget()-awayTeam.getBudget();
		System.out.println("MATCH: "+homeTeam.getName()+" vs "+awayTeam.getName()+": "+homeScore
				+"-"+awayScore+"\n");
		//TENDRÉ QUE DEFINIR ARRAYS DE PROBABILIDADES EN FUNCIÓN DE LA DIF DE PRESUPUESTO
		if(difBudget>HIGH_DIF) {
			arrProb[0]=0.15;
			arrProb[1]=0.5;
			arrProb[2]=0.2;
			arrProb[3]=0.1;
			arrProb[4]=0.05;
		}else if(difBudget>LOW_DIF) {
			arrProb[0]=0.1;
			arrProb[1]=0.4;
			arrProb[2]=0.3;
			arrProb[3]=0.15;
			arrProb[4]=0.05;
		}else {
			arrProb[0]=0.05;
			arrProb[1]=0.3;
			arrProb[2]=0.4;
			arrProb[3]=0.2;
			arrProb[4]=0.05;
		}
		//IMPRIMO Nº DEL 0 AL 100 PARA QUE LA PROBABILIDAD DE QUE ESTÉ EN UNA PARTE DE LA DISTRIBUCIÓN COINCIDA CON LOS ARRAYS DE PROBABILIDAD 
		double rand = new Random().nextInt(101);
		rand=rand/100;
		goles=(int)rand*7/100; //LO TRANSFORMO EN PROPORCIÓN A UN Nº DEL O-7 (GOLES)
		//SE IRÁ MIRANDO EN QUE PARTE DE LA CAMPANA DE DISTRIBUCIÓN ESTÁ DICHO Nº (IREMOS SUMANDO Y SE OBSERVARÁ QUE EN LA MITAD SE CONCENTRAN MÁS NUMEROS POR LO QUE ES MÁS PROBABLE QUE TOQUE AHÍ)
		if (rand < arrProb[0]) {
            System.out.println((difBudget > 0 ? homeTeam.getName() : awayTeam.getName()) + " wins by a landslide");
            if(homeTeam.getBudget()>awayTeam.getBudget()) {
            	homeScore=goles;
            	awayScore=new Random().nextInt(homeScore-2);
            }
            else {
            	awayScore=goles;
            	homeScore=new Random().nextInt(awayScore-2);
            }
		} else if (rand <arrProb[0]+arrProb[1]) {
            System.out.println((difBudget> 0 ? homeTeam.getName() : awayTeam.getName()) + " wins");
            if(homeTeam.getBudget()>awayTeam.getBudget()) {
            	homeScore=goles;
            	awayScore=new Random().nextInt(2)+homeScore-2;
            }
            else {
            	awayScore=goles;
            	homeScore=new Random().nextInt(2)+awayScore-2;
            }
        } else if (rand <arrProb[0]+arrProb[1]+arrProb[2]) {
           System.out.println("It´s a draw");
           homeScore=goles;
           awayScore=goles;
           
        } else if (rand < arrProb[0]+arrProb[1]+arrProb[2]+arrProb[3]) {
           System.out.println((difBudget< 0 ? homeTeam.getName() : awayTeam.getName()) + " wins");
           if(homeTeam.getBudget()>awayTeam.getBudget()) {
           	awayScore=goles;
           	homeScore=new Random().nextInt(2)+awayScore-2;
           }
           else {
           	homeScore=goles;
           	awayScore=new Random().nextInt(2)+homeScore-2;
           }
        } else {
            System.out.println((difBudget < 0 ?  homeTeam.getName() : awayTeam.getName()) + " wins by a landslide");
            if(homeTeam.getBudget()>awayTeam.getBudget()) {
            	awayScore=goles;
            	homeScore=new Random().nextInt(awayScore-2);
            }
            else {
            	homeScore=goles;
            	awayScore=new Random().nextInt(homeScore-2);
            }
        }
		
		//AÑADIMOS LOS GOLES A LOS EQUIPOS
		homeTeam.addGoalsFor(homeScore);
		homeTeam.addGoalsAgainst(awayScore);
		awayTeam.addGoalsFor(awayScore);
		awayTeam.addGoalsAgainst(homeScore);
	}
	
}
