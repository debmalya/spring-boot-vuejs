package jash.debmalya.springbootvuejs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Score {
	private String winner;
	private String duration;
	private TimeBasedScore fullTime;
	private TimeBasedScore halfTime;
	private TimeBasedScore extraTime;
	
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public TimeBasedScore getFullTime() {
		return fullTime;
	}
	public void setFullTime(TimeBasedScore fullTime) {
		this.fullTime = fullTime;
	}
	public TimeBasedScore getHalfTime() {
		return halfTime;
	}
	public void setHalfTime(TimeBasedScore halfTime) {
		this.halfTime = halfTime;
	}
	public TimeBasedScore getExtraTime() {
		return extraTime;
	}
	public void setExtraTime(TimeBasedScore extraTime) {
		this.extraTime = extraTime;
	}
	public Score(){
		
	}
	@Override
	public String toString() {
		return "Score [winner=" + winner + ", duration=" + duration + "]";
	}
	
	
}
