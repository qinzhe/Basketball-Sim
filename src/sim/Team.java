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
	
	public void refresh() {
		for (int i = 0; i < roster.size(); i++) {
			roster.get(i).gameStamina = 0;
			roster.get(i).SEC = 0;
		}
	}

	public void nameTeam(int i) {
		try {
			String location = Files.readAllLines(Paths.get("src/cities.txt"))
					.get(i);
			String name = Files.readAllLines(Paths.get("src/teams.txt")).get(i);
			String abb = Files.readAllLines(Paths.get("src/abbreviations.txt"))
					.get(i);
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