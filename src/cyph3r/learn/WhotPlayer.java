package cyph3r.learn;

import java.util.ArrayList;
import java.util.HashSet;

public class WhotPlayer {
	Hand hand;
	private String name;
	private int score = 0;

	WhotPlayer(String name) {
		if (name != null)
			this.name = name;
		else
			throw new IllegalArgumentException("Name can not be null");
		this.hand = new Hand();
	}

	public WhotPlayer(String name, Card[] hand) {
		if (name != null)
			this.name = name;
		else
			throw new IllegalArgumentException("Name can not be null");
		this.hand = new Hand(hand);
	}

	public String getName() {
		return this.name;
	}


	public int getPlayerScore() {
		return this.score;
	}

	public void updatePlayerScore(int points) {
		this.score += points;
	}

	public void receiveCard(Card card) {
		this.hand.receiveCard(card);
	}

	public Card dealCard() {
		return this.hand.playCard();
	}

	public Card dealCard(int ordinal) {
		System.out.println("The current value is " + ordinal);
		return this.hand.playCard(ordinal);
	}

	public Card[] dealCards(int... indices) {
		return this.hand.playCards(indices);
	}

	int[] playAsHuman(Card onDeck, int numOfPlayers, int numOfCardsInMarket, boolean isTenderEnabled,
	                  String message) {
		ArrayList<Integer> indexToReturn = new ArrayList<>();
		HashSet<Integer> toCheck = new HashSet<>();
		boolean exit = false;
		System.out.println(this.hand);
		System.out.println();
		System.out.println(message);
		first:
		while (!exit) {
			System.out.println("Card on deck: " + onDeck);
			System.out.println("Cards left in market: " + numOfCardsInMarket);
			System.out.print(
					"Enter 'm' to go to market or the card number or number separated by a space to play. Your move: ");
			String move = TextIO.getlnString().trim();
			System.out.println();
			if (move.equalsIgnoreCase("m")) {
				indexToReturn.add(-1);
				return new int[]{-1};
			}

			toCheck.clear();
			indexToReturn.clear();
			String[] move_split = move.split(" ");
			int index_int;
			for (String index : move_split) {
				try {
					index_int = Integer.parseInt(index);//todo: ensure that the WhotGame is using the returned indexes correctly
					if (!toCheck.add(index_int)) {
						System.out.println(this.hand);
						System.out.println();
						System.out.println("Sorry, your input contains duplicate indices. Let's try again\n");
						continue first;
					}
					if (index_int > this.hand.getHandSize()) {
						System.out.println(this.hand);
						System.out.println();
						System.out.println("Sorry, your input or one of your input is greater than your hand size.");
						System.out.println(
								"Ensure your index(ces) is/are in range of (1 - " + this.hand.getHandSize() + ").\n");
						continue first;
					}

				} catch (NumberFormatException e) {
					System.out.println(this.hand);
					System.out.println();
					if (index.equalsIgnoreCase(""))
						System.out.println("You didn't anything");
					else
						System.out.println("Sorry, wrong input: " + index + ".");
					System.out.println("Ensure your index(dices) is/are in range of (1 - " + this.hand.getHandSize() + ").");
					System.out.println("Let's try that again.\n");
					continue first;
				}
				indexToReturn.add(Integer.parseInt(index) - 1);
			}
			exit = true;
		}
		return indexToReturn.stream().mapToInt(Integer::intValue).toArray();
	}

	public Object[] playAsComputer(Card onDeck, int numOfPlayers, int numOfCardsInMarket, boolean isTenderEnabled) {
		return new Object[]{};
	}

	public String getPlayerHand() {
		return this.hand.toString();
	}

	public int getPlayerCardWeight() {
		return this.hand.getCardWeight();
	}

}
