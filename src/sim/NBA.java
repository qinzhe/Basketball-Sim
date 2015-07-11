package sim;

import java.io.IOException;
import java.util.Random;

public class NBA {

	static Team league[] = new Team[30];

	static Random random = new Random();

	public static void main(String[] args) throws IOException {
		newLeague();
		new GameSim(0, random.nextInt(28)+1);
		new GameSim(0, random.nextInt(28)+1);
		for (int i = 0; i < league[0].roster.size(); i++) {
			System.out.println(league[0].roster.get(i).lastName + ": "
					+ league[0].roster.get(i).sPTS);
		}
	}

	private static void newLeague() throws IOException {
		for (int i = 0; i < 30; i++) {
			league[i] = new Team();
			league[i].nameTeam(i);
			for (int j = 0; j < 15; j++) {
				league[i].roster.add(randomPlayer(league[i].abb));
			}
		}
		league[0].userControlled = true;
	}

	private static Player randomPlayer(String team) {
		Player player = new Player();
		try {
			player.giveName();
		} catch (IOException e) {
			player.lastName = "Baller";
			player.firstName = "Joe";
		}
		player.pos = random.nextInt(5);
		player.team = team;
		player.offRTG = random.nextInt(60) + 35;
		player.defRTG = random.nextInt(60) + 35;
		player.stamina = random.nextInt(12);
		return player;
	}

}