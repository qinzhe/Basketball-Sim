package sim;

import java.util.ArrayList;
import java.util.Random;

public class Game {

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

	public Game(int h, int a) {
		home = h;
		away = a;
		tipOff();
	}

	public void tipOff() {
		for (int i = 0; i < 15; i++) {
			homeBench.add(NBA.league[home].roster.get(i));
			awayBench.add(NBA.league[away].roster.get(i));
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

		while (gameClock > 0) {
			gameClock -= 1;
			shotClock -= 1;
			if (random.nextInt(12) > shotClock) {
				shoot((random.nextInt(3) + 4) / 2);
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
		quarter += 1;
		if (quarter > 4) {
			endGame();
		} else {
			System.out.println("Quarter " + quarter + " has ended.");
			gameClock = 720;
		}
		
	}

	private void endGame() {

		System.out.println("THE GAME HAS ENDED");

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

	}

	public void shoot(int points) {

		float shotChance;
		if (ballHandler < 5) {
			shotChance = homeActive[ballHandler].offRTG;
		} else {
			shotChance = awayActive[ballHandler - 5].offRTG;
		}

		if (random.nextInt(100) < shotChance) {
			if (homeBall) {
				homeScore += points;
				homeActive[ballHandler].PTS += points;
				homeActive[ballHandler].FGM += 1;
				homeActive[ballHandler].FGA += 1;
				System.out.println(gameClock / 60 + ":" + gameClock % 60
						+ " - (" + NBA.league[home].abb + ") "
						+ homeActive[ballHandler].lastName + " makes " + points
						+ " pt shot. " + awayScore + "-" + homeScore);
			} else {
				awayScore += points;
				awayActive[ballHandler - 5].PTS += points;
				awayActive[ballHandler - 5].FGM += 1;
				awayActive[ballHandler - 5].FGA += 1;
				System.out.println(gameClock / 60 + ":" + gameClock % 60
						+ " - (" + NBA.league[away].abb + ") "
						+ awayActive[ballHandler - 5].lastName + " makes "
						+ points + " pt shot. " + awayScore + "-" + homeScore);
			}
			nextPossession();
		} else {
			if (homeBall) {
				homeActive[ballHandler].FGA += 1;
			} else {
				awayActive[ballHandler - 5].FGA += 1;
			}
			System.out.println("Missed shot.");
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
