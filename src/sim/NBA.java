package sim;

import java.util.Random;

public class NBA {

	static Team league[] = new Team[30];

	static Random random = new Random();

	public static void main(String[] args) {
		newLeague();
		new Game(0,1);
	}

	public static void newLeague() {
		for (int i = 0; i < 30; i++) {
			league[i] = new Team();
			for (int j = 0; j < 15; j++) {
				league[i].roster.add(randomPlayer(league[i].abb));
			}
		}
		league[0].userControlled = true;
	}

	public static Player randomPlayer(String team) {
		Player player = new Player();
		player.firstName = "Joe";
		player.lastName = "Jones";
		player.pos = random.nextInt(5);
		player.team = team;
		player.offRTG = random.nextInt(100);
		player.defRTG = random.nextInt(100);
		return player;
	}

}