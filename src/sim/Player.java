package sim;

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
	// float form = 1;
	boolean injured = false;
	int injuryDuration = 0;

	public Player() {

	}
	
	public String getLastName(){
		return this.lastName;
	}

}