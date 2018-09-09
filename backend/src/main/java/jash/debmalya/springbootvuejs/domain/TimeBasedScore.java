package jash.debmalya.springbootvuejs.domain;

public class TimeBasedScore {
		private Integer homeTeam;
		private Integer awayTeam;
		
		
		public Integer getHomeTeam() {
			return homeTeam;
		}
		public void setHomeTeam(Integer homeTeam) {
			this.homeTeam = homeTeam;
		}
		public Integer getAwayTeam() {
			return awayTeam;
		}
		public void setAwayTeam(Integer awayTeam) {
			this.awayTeam = awayTeam;
		}
		public TimeBasedScore(){
			
		}
		@Override
		public String toString() {
			return "TimeBasedScore [homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + "]";
		}
		
		
}
