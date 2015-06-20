package sim;

import java.util.ArrayList;

public class Team {

	String location, name, abb, arena;
	int wins, losses = 0;
	ArrayList<Player> roster = new ArrayList<Player>();
	boolean userControlled = false;

	public Team() {
		abb = "NBA";
	}

}