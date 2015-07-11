package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GameSim extends NBA {

	int home;
	int away;
	Player homeActive[] = new Player[5];
	Player awayActive[] = new Player[5];
	ArrayList<Player> homeBench = new ArrayList<Player>();
	ArrayList<Player> awayBench = new ArrayList<Player>();
	int shotClock = 24;
	int gameClock = 60 * 12 * 4;
	int minutes = 48;
	int seconds = 0;
	int homeScore = 0;
	int awayScore = 0;
	boolean homeBall;
	int ballHandler;
	int temp = -1;
	int simSpeed = 1;
	Random random = new Random();

	public GameSim(int h, int a) {
		home = h;
		away = a;
		tipOff();
	}

	private void tipOff() {
		for (int i = 0; i < 15; i++) {
			homeBench.add(league[home].roster.get(i));
			awayBench.add(league[away].roster.get(i));
		}
		for (int i = 0; i < 5; i++) {
			homeActive[i] = homeBench.get(0);
			homeBench.remove(0);
			awayActive[i] = awayBench.get(i);
			awayBench.remove(0);
		}
		if (random.nextBoolean()) {
			ballHandler = random.nextInt(5);
			homeBall = true;
		} else {
			ballHandler = random.nextInt(5) + 5;
			homeBall = false;
		}
		play();
		score();
	}

	private void score() {
		if (homeScore > awayScore) {
			league[home].wins += 1;
			league[away].losses += 1;
			endGame(home);
		} else if (awayScore > homeScore) {
			league[away].wins += 1;
			league[home].losses += 1;
			endGame(away);
		} else {
			gameClock = 360;
			play();
			score();
		}
	}

	private void play() {

		while (gameClock > 0) {
			gameClock -= 1;
			seconds -= 1;
			if (seconds < 0) {
				minutes -= 1;
				seconds = 59;
			}
			shotClock -= 1;
			if (random.nextInt(16) > shotClock) {
				shoot((random.nextInt(3) + 4) / 2);
			} else {
				pass();
			}
			try {
				Thread.sleep(simSpeed);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (shotClock == 0) {
				turnover("TO");
			}

		}

	}

	private void endGame(int winner) {

		System.out.println(league[winner].abb + " win.");

		// Return all players to the bench
		for (int i = 0; i < 5; i++) {
			homeBench.add(homeActive[i]);
			awayBench.add(awayActive[i]);
		}

		sortPlayers();
		recordStats();

	}

	private void sortPlayers() {

		Comparator<Player> lastName = new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				return arg0.getLastName().compareTo(arg1.getLastName());
			}
		};

		Collections.sort(homeBench, lastName);
		Collections.sort(awayBench, lastName);
		Collections.sort(league[home].roster, lastName);
		Collections.sort(league[away].roster, lastName);

	}

	private void recordStats() {
		for (int i = 0; i < 15; i++) {
			if (league[home].roster.get(i) != null) {
				league[home].roster.get(i).sPTS += homeBench.get(i).PTS;
				league[home].roster.get(i).sAST += homeBench.get(i).AST;
				league[home].roster.get(i).sSEC += homeBench.get(i).SEC;
				league[home].roster.get(i).sORB += homeBench.get(i).ORB;
				league[home].roster.get(i).sDRB += homeBench.get(i).DRB;
				league[home].roster.get(i).sFGA += homeBench.get(i).FGA;
				league[home].roster.get(i).sFGM += homeBench.get(i).FGM;
			}
			if (league[away].roster.get(i) != null) {
				league[away].roster.get(i).sPTS += awayBench.get(i).PTS;
				league[away].roster.get(i).sAST += awayBench.get(i).AST;
				league[away].roster.get(i).sSEC += awayBench.get(i).SEC;
				league[away].roster.get(i).sORB += awayBench.get(i).ORB;
				league[away].roster.get(i).sDRB += awayBench.get(i).DRB;
				league[away].roster.get(i).sFGA += awayBench.get(i).FGA;
				league[away].roster.get(i).sFGM += awayBench.get(i).FGM;
			}
		}
	}

	private void rebound() {

		temp = ballHandler;

		ballHandler = random.nextInt(10);

		if (homeBall) {
			if (ballHandler >= 5) {
				awayActive[ballHandler - 5].DRB += 1;
				nextPossession();
			} else {
				homeActive[ballHandler].ORB += 1;
				shotClock = 16;
			}
		} else {
			if (ballHandler >= 5) {
				awayActive[ballHandler - 5].ORB += 1;
				shotClock = 16;
			} else {
				homeActive[ballHandler].DRB += 1;
				nextPossession();
			}
		}

	}

	private void pass() {

		temp = ballHandler;

		if (random.nextBoolean()) {

			if (ballHandler < 5) {
				while (ballHandler == temp) {
					ballHandler = random.nextInt(5);
				}
			} else {
				while (ballHandler == temp) {
					ballHandler = random.nextInt(5) + 5;
				}
			}

		}

	}

	private void shoot(int points) {

		float shotChance;
		float defense;
		if (ballHandler < 5) {
			shotChance = homeActive[ballHandler].offRTG;
			defense = awayActive[ballHandler].defRTG;
		} else {
			shotChance = awayActive[ballHandler - 5].offRTG;
			defense = homeActive[ballHandler - 5].defRTG;
		}

		if (random.nextInt(100) < defense) {
			shotChance *= .5;
		}

		if (points == 3) {
			shotChance *= .4;
		} else {
			shotChance *= .7;
		}

		if (random.nextInt(100) < shotChance) {
			if (homeBall) {
				homeScore += points;
				homeActive[ballHandler].PTS += points;
				homeActive[ballHandler].FGM += 1;
				homeActive[ballHandler].FGA += 1;
				if ((temp < 5) && (temp > -1) && (temp != ballHandler)) {
					homeActive[temp].AST += 1;
					System.out.println(String.format("%02d:%02d", minutes,
							seconds)
							+ " - ("
							+ league[home].abb
							+ ") "
							+ homeActive[ballHandler].lastName
							+ " makes "
							+ points
							+ " pt shot. "
							+ homeActive[temp].lastName
							+ " assists. " + awayScore + "-" + homeScore);
				} else {
					System.out.println(String.format("%02d:%02d", minutes,
							seconds)
							+ " - ("
							+ league[home].abb
							+ ") "
							+ homeActive[ballHandler].lastName
							+ " makes "
							+ points
							+ " pt shot. "
							+ awayScore
							+ "-"
							+ homeScore);
				}
			} else {
				awayScore += points;
				awayActive[ballHandler - 5].PTS += points;
				awayActive[ballHandler - 5].FGM += 1;
				awayActive[ballHandler - 5].FGA += 1;
				if ((temp >= 5) && (temp != ballHandler)) {
					awayActive[temp - 5].AST += 1;
					System.out.println(gameClock / 60 + ":" + gameClock % 60
							+ " - (" + league[home].abb + ") "
							+ awayActive[ballHandler - 5].lastName + " makes "
							+ points + " pt shot. "
							+ awayActive[temp - 5].lastName + " assists. "
							+ awayScore + "-" + homeScore);
				} else {
					System.out.println(gameClock / 60 + ":" + gameClock % 60
							+ " - (" + league[away].abb + ") "
							+ awayActive[ballHandler - 5].lastName + " makes "
							+ points + " pt shot. " + awayScore + "-"
							+ homeScore);
				}
			}
			nextPossession();
		} else {
			if (homeBall) {
				homeActive[ballHandler].FGA += 1;
				System.out.println(gameClock / 60 + ":" + gameClock % 60
						+ " - (" + league[home].abb + ") "
						+ homeActive[ballHandler].lastName + " missed shot.");
			} else {
				awayActive[ballHandler - 5].FGA += 1;
				System.out.println(gameClock / 60 + ":" + gameClock % 60
						+ " - (" + league[away].abb + ") "
						+ awayActive[ballHandler - 5].lastName
						+ " missed shot.");
			}
			rebound();
		}

	}

	private void nextPossession() {

		if (homeBall) {
			homeBall = false;
			ballHandler = 5;
		} else {
			homeBall = true;
			ballHandler = 0;
		}

		temp = -1;
		shotClock = 24;
		play();

	}

	private void turnover(String TO) {

		if (homeBall) {
			if (TO == "TO") {
				homeActive[ballHandler].TO += 1;
				nextPossession();
			} else if (TO == "STL") {
				awayActive[temp - 5].STL += 1;
				nextPossession();
			}
			homeBall = false;
		} else {
			if (TO == "TO") {
				awayActive[ballHandler - 5].TO += 1;
				nextPossession();
			} else if (TO == "STL") {
				homeActive[temp].STL += 1;
				nextPossession();
			}
			homeBall = true;
		}

		ballHandler = temp;
		temp = -1;

	}

	private void substitutions() {
		// TODO
	}

}
