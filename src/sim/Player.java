package sim;

public class Player {

	String firstName, lastName, team;
	int pos;
	// game stats:
	int PTS, FGA, FGM, AST, BLK, ORB, DRB, TO, STL, FL, MIN = 0;
	// total season stats:
	int sPTS, sFGA, sFGM, sAST, sBLK, sORB, sDRB, sTO, sSTL, sFL, sMIN, GP = 0;
	// attributes:
	int offRTG, defRTG;
	// float form = 1;
	boolean injured = false;
	int injuryDuration = 0;

	public Player() {

	}

}