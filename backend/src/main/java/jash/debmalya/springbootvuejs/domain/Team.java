package jash.debmalya.springbootvuejs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Team(){
		
	}

	@Override
	public String toString() {
		return "Team [name=" + name + "]";
	}
	
	
}	
