package sim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Player {

	String firstName, lastName, team;
	int pos;
	// game stats:
	int PTS, FGA, FGM, AST, BLK, ORB, DRB, TO, STL, FL, SEC = 0;
	int gameStamina;
	// total season stats:
	int sPTS, sFGA, sFGM, sAST, sBLK, sORB, sDRB, sTO, sSTL, sFL, sSEC, GP = 0;
	// attributes:
	int offRTG, defRTG, stamina;
	int RTG = (offRTG + defRTG) / 2;
	// float form = 1;
	boolean injured = false;
	int injuryDuration = 0;

	public Player() {

	}

	public void giveName() throws IOException {
		Random random = new Random();
		String firstName = "John";
		firstName = Files.readAllLines(Paths.get("src/firstNames.txt"))
				.get(random.nextInt(220));
		String lastName = "Smith";
		lastName = Files.readAllLines(Paths.get("src/lastNames.txt")).get(
				random.nextInt(150));
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getLastName() {
		return this.lastName;
	}

}