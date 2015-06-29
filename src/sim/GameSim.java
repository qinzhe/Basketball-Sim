package sim;

import java.util.ArrayList;
import java.util.Random;

public class GameSim extends NBA {

	int home;
	int away;
	Player homeActive[] = new Player[5];
	Player awayActive[] = new Player[5];
	ArrayList<Player> homeBench = new ArrayList<Player>();
	ArrayList<Player> awayBench = new ArrayList<Player>();
	int shotClock = 24;
	int quarter = 1;
	int gameClock = 720;
	int homeScore = 0;
	int awayScore = 0;
	boolean homeBall;
	int ballHandler;
	int temp = -1;
	Random random = new Random();

	public GameSim(int h, int a) {
		home = h;
		away = a;
		tipOff();
	}

	public void tipOff() {
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
	}

	public void play() {

		while (quarter <= 4) {

			while (gameClock > 0) {
				gameClock -= 1;
				shotClock -= 1;
				if (random.nextInt(16) > shotClock) {
					shoot((random.nextInt(3) + 4) / 2);
				} else {
					pass();
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				if (shotClock == 0) {
					turnover("TO");
				}
			}

			if (quarter == 4) {
				if (homeScore > awayScore) {
					league[home].wins += 1;
					league[away].losses += 1;
					endGame(home);
					break;
				} else {
					league[away].wins += 1;
					league[home].losses += 1;
					endGame(away);
					break;
				}
			}

			// System.out.println("Quarter " + quarter + " has ended.");
			quarter += 1;
			gameClock = 720;

		}

		/*
		 * if (homeScore != awayScore) { endGame(); } else { quarter = 4;
		 * gameClock = 360; play(); }
		 */

	}

	private void endGame(int winner) {
		//System.out.println(league[winner].abb + " win.");
	}

	public void rebound() {

		temp = ballHandler;

		ballHandler = random.nextInt(10);

		if (homeBall) {
			if (ballHandler >= 5) {
				awayActive[ballHandler - 5].DRB += 1;
				nextPossession();
			} else {
				homeActive[ballHandler].ORB += 1;
			}
		} else {
			if (ballHandler >= 5) {
				awayActive[ballHandler - 5].ORB += 1;
			} else {
				homeActive[ballHandler].DRB += 1;
				nextPossession();
			}
		}

	}

	public void pass() {

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

	public void shoot(int points) {

		float shotChance;
		if (ballHandler < 5) {
			shotChance = homeActive[ballHandler].offRTG;
		} else {
			shotChance = awayActive[ballHandler - 5].offRTG;
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
					System.out.println("Q" + quarter + " " + gameClock / 60
							+ ":" + gameClock % 60 + " - (" + league[home].abb
							+ ") " + homeActive[ballHandler].lastName
							+ " makes " + points + " pt shot. "
							+ homeActive[temp].lastName + " assists. "
							+ awayScore + "-" + homeScore);
				} else {
					System.out.println("Q" + quarter + " " + gameClock / 60
							+ ":" + gameClock % 60 + " - (" + league[home].abb
							+ ") " + homeActive[ballHandler].lastName
							+ " makes " + points + " pt shot. " + awayScore
							+ "-" + homeScore);
				}
			} else {
				awayScore += points;
				awayActive[ballHandler - 5].PTS += points;
				awayActive[ballHandler - 5].FGM += 1;
				awayActive[ballHandler - 5].FGA += 1;
				if ((temp >= 5) && (temp != ballHandler)) {
					awayActive[temp - 5].AST += 1;
					System.out.println("Q" + quarter + " " + gameClock / 60
							+ ":" + gameClock % 60 + " - (" + league[home].abb
							+ ") " + awayActive[ballHandler - 5].lastName
							+ " makes " + points + " pt shot. "
							+ awayActive[temp - 5].lastName + " assists. "
							+ awayScore + "-" + homeScore);
				} else {
					System.out.println("Q" + quarter + " " + gameClock / 60
							+ ":" + gameClock % 60 + " - (" + league[away].abb
							+ ") " + awayActive[ballHandler - 5].lastName
							+ " makes " + points + " pt shot. " + awayScore
							+ "-" + homeScore);
				}
			}
			nextPossession();
		} else {
			if (homeBall) {
				homeActive[ballHandler].FGA += 1;
				System.out.println("Q" + quarter + " " + gameClock / 60 + ":"
						+ gameClock % 60 + " - (" + league[home].abb + ") "
						+ homeActive[ballHandler].lastName + " missed shot.");
			} else {
				awayActive[ballHandler - 5].FGA += 1;
				System.out.println("Q" + quarter + " " + gameClock / 60 + ":"
						+ gameClock % 60 + " - (" + league[away].abb + ") "
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

	public void turnover(String TO) {

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

	public void substitutions() {

	}

}
