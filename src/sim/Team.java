package sim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Team {

	String location, name, abb, arena;
	int wins, losses = 0;
	ArrayList<Player> roster = new ArrayList<Player>();
	boolean userControlled = false;

	public Team() {
		abb = "NBA";
	}
	
	public void nameTeam(int i) {
		try {
			String location = Files.readAllLines(Paths.get("src/sim/cities.txt")).get(i);
			String name = Files.readAllLines(Paths.get("src/sim/teams.txt")).get(i);
			String abb = Files.readAllLines(Paths.get("src/sim/abbreviations.txt")).get(i);
			this.location = location;
			this.name = name;
			this.abb = abb;
		} catch (IOException e) {
			this.location = "USA";
			this.name = "Ballers";
			this.abb = "USA";
		}
	}

}