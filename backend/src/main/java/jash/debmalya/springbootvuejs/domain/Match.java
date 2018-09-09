package jash.debmalya.springbootvuejs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
	private Team homeTeam;
	private Team awayTeam;
	private Score score;
	private String lastUpdated;
	
	public String getLastUpdated() {
		return lastUpdated;
	}



	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}



	public Team getHomeTeam() {
		return homeTeam;
	}
	
	
	
	public Team getAwayTeam() {
		return awayTeam;
	}



	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}



	public Score getScore() {
		return score;
	}



	public void setScore(Score score) {
		this.score = score;
	}



	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}



	public Match(){
		
	}
	

	
	
}
