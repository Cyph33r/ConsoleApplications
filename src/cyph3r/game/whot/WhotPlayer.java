package cyph3r.game.whot;

import textio.TextIO;

import java.util.ArrayList;
import java.util.HashSet;


//todo: try to add players names in front of error prompts
class WhotPlayer {
	private Hand hand;
	private String name;
	private int score = 0;
	private Hand outgoingCards = new Hand();

	WhotPlayer(String name) {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null");
		this.name = name;
		this.hand = new Hand();
	}


	@Override
	public String toString() {
		return this.getName();
	}

	String getName() {
		return this.name;
	}

	int getNumOfCardsInHand() {
		return this.hand.getHandSize();
	}
	int getPlayerScore() {
		return this.score;
	}

	void updatePlayerScore(int points) {
		this.score += points;
	}

	void receiveCard(Card card) {
		this.hand.receiveCard(card);
	}

	Card[] flushOutgoingCards() {
		return this.hand.dealCards(this.outgoingCards.toArray());
	}

	void clearCards() {
		this.hand.clearCards();
	}

	Card[] playAsHuman(Card onDeck, int numOfCardsInMarket, String message) {
		HashSet<Integer> toCheck = new HashSet<>();
		ArrayList<Integer> indexToReturn = new ArrayList<>();
		this.hand.shuffleHand();
		System.out.println(this.hand);
		System.out.println();
		System.out.println(message);
		first:
		while (true) {
			System.out.println("Card on deck: " + onDeck);
			System.out.println("Cards left in market: " + numOfCardsInMarket);
			System.out.print(
					"Enter 'm' to go to market or the card number or numbers separated by a space to play. Your move: ");
			String move = TextIO.getlnString().trim();
			if (move.equalsIgnoreCase("m") || move.equals("0")) {
				this.outgoingCards.clearCards();
				return new Card[1];
			}
			toCheck.clear();
			indexToReturn.clear();
			String[] move_split = move.trim().split("\\s++");
			int index_int;
			for (String index : move_split) {
				try {
					index_int = Integer.parseInt(index);
					if (!toCheck.add(index_int)) {
						System.out.println();
						System.out.println(this.hand);
						System.out.println();
						System.out.println("Sorry, your input contains duplicate indices. Let's try again\n");
						continue first;
					}
					if (index_int > this.hand.getHandSize()) {
						System.out.println();
						System.out.println(this.hand);
						System.out.println();
						System.out.println("Sorry, all your input must less than or equal than your hand size.");
						System.out.println(
								"Ensure your input(s) is/are in range of (1 - " + this.hand.getHandSize() + ").\n");
						continue first;
					}
					if (index_int <= 0) {
						System.out.println();
						System.out.println(this.hand);
						System.out.println();
						System.out.println("Sorry, all your inputs must be greater than zero.");
						System.out.println(
								"Ensure your index(ces) is/are in range of (1 - " + this.hand.getHandSize() + ").\n");
						continue first;
					}
				} catch (NumberFormatException e) {
					System.out.println();
					System.out.println(this.hand);
					System.out.println();
					if (index.equalsIgnoreCase(""))
						System.out.println("You didn't enter anything");
					else
						System.out.println("Sorry, wrong input: " + index + ".");
					System.out.println("Ensure your index(dices) is/are in range of (1 - " + this.hand.getHandSize() + ").");
					System.out.println("Let's try that again.\n");
					continue first;
				}
				indexToReturn.add(Integer.parseInt(index) - 1);
			}
			break;
		}
		this.outgoingCards.clearCards();
		Card[] outgoing = indexToReturn.stream().map((a) -> this.hand.peek(a)).toArray(Card[]::new);
		this.outgoingCards.receiveCards(outgoing);
		return outgoing;
	}

	public Card[] playAsComputer(Card onDeck, int numOfPlayers, int numOfCardsInMarket, boolean isTenderEnabled) {
		return new Card[0];
	}

	String getPlayerHand() {
		return this.hand.toString();
	}

	boolean handEmpty() {
		return this.hand.isEmpty();
	}

	int getPlayerCardWeight() {
		return this.hand.getCardWeight();
	}

}
