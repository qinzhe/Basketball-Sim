package sim;

import java.util.Random;

public class NBA {

	static Team league[] = new Team[30];

	static Random random = new Random();

	public static void main(String[] args) {
		newLeague();
		new GameSim(0,1);
		new GameSim(0,1);
		for (int i = 0; i < league[0].roster.size(); i++){
			System.out.println(league[0].roster.get(i).lastName+": "+league[0].roster.get(i).sPTS);
		}
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
		player.lastName = "Jones" + random.nextInt(99);
		player.pos = random.nextInt(5);
		player.team = team;
		player.offRTG = random.nextInt(60) + 35;
		player.defRTG = random.nextInt(60) + 35;
		player.stamina = random.nextInt(12);
		return player;
	}

}