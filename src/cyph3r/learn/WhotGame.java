package cyph3r.learn;

import cyph3r.learn.Card.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

//todo: Clean up all output.
// fixme: modify for case in which whot is the first card

public class WhotGame {
	private final ArrayList<WhotPlayer> players = new ArrayList<>();
	private final boolean tenderEnabled;
	int pickTwo = 0;
	private Hand market = new Hand();
	private Hand deck = new Hand();
	private Card topCard;
	private WhotPlayer playerTurn;
	private int playerTurnIndex = 0;
	private int roundCount = 0;
	private boolean gameOver = false;
	private ArrayList<Object> iNeed;


	public WhotGame(int playerNum, int initCardNum, int numOfPacks, boolean tenderEnabled) {
		String welcomeMessage = ""; //todo: Fill this in the game intro later
		System.out.println(welcomeMessage);
		if (numOfPacks <= 0 || (playerNum * initCardNum > (int) (0.75 * numOfPacks * 75))) // Ensure that the number of cards to be distributed is not more than 75% of the total cards available
			throw new IllegalArgumentException("Not enough cards in market to share");
		if (numOfPacks > 4)
			throw new IllegalArgumentException("You are not allowed use more than 4 packs for a game");
		if (playerNum < 2)
			throw new IllegalArgumentException("Not enough players to start a game");
		this.tenderEnabled = tenderEnabled;
		shape[] theShapes = Card.shape.values();
		iNeed = new ArrayList<>();
		for (int packNumber = 0; packNumber < numOfPacks; ++packNumber) {
			for (shape theShape : theShapes) {
				if (theShape == shape.WHOT)
					continue;
				for (int cardValue = 1; cardValue <= 14; ++cardValue) {
					this.market.receiveCard(new Card(theShape, cardValue));
				}
			}
			for (int whotNumber = 0; whotNumber <= 4; ++whotNumber)
				this.market.receiveCard(new Card());
		}
		this.market.shuffleHand();
//		System.out.println(this.market); //todo: Remove this after testing
		this.addCardToDeckFromMarket();
		for (int i = 0; i < playerNum; ++i) {
			System.out.print("Player " + (i + 1) + " enter your name: ");
			String name = TextIO.getlnWord();
			this.players.add(new WhotPlayer(name));

			for (int j = 0; j < initCardNum; ++j)
				dealPlayerCard(this.players.get(i), 1);
		}
		this.playerTurn = this.players.get(0);


	}

	public static boolean compareCardsByNumber(Card card1, Card card2) {
		return card1.getValue() == card2.getValue();

	}

	public static boolean compareCardsByShape(Card card1, Card card2) {
		return card1.getShape() == card2.getShape();
	}

	private void dealCurrentPlayerCard(int times) {
		this.dealPlayerCard(this.playerTurn, times);
	}

	private void dealPlayerCard(WhotPlayer player, int times) {
		for (int i = 0; i < times; ++i)
			player.receiveCard(this.market.playCard());
	}

	private void addCardToDeckFromMarket() {
		Card card = this.market.playCard();
		this.topCard = card;
		this.deck.receiveCard(card);
	}

	private void resetDeck() {
		Card[] underDeck = this.deck.playCards(0, this.deck.getHandSize() - 1);
		this.market.receiveCards(underDeck);
	}

	private void addCardToDeckFromPlayer(Card card) {
		this.addCardsToDeckFromPlayer(new Card[]{card});
	}

	private void addCardsToDeckFromPlayer(Card[] cards) {
		for (Card card : cards) {
			this.deck.receiveCard(card);
			this.topCard = card;
		}
	}
	//todo: ensure that the pick 2 method doesn't break the game

	private void generalMarket() {
		for (WhotPlayer player : this.players) {
			if (player == this.playerTurn)
				continue;
			System.out.println(String.format("%s goes to market.", player.getName()));
			this.dealPlayerCard(player, 1);
		}
	}

	private void updatePlayerTurn() {
		++this.playerTurnIndex;
		this.roundCount++;
		if (this.playerTurnIndex >= this.players.size()) {
			this.playerTurnIndex = 0;
		}
		this.playerTurn = this.players.get(playerTurnIndex);
	}

	public String toString() {
		ArrayList<WhotPlayer> players_copy = new ArrayList<>(this.players);
		players_copy.sort(new SortPlayersByScore().reversed());
		StringBuilder scoreBoard = new StringBuilder(String.format("%-18s %2$s\n", "Player Name", "Score"));
		for (WhotPlayer player : players_copy) {
			String name = player.getName();
			if (name.length() > 18)
				name = name.substring(0, 15) + "...";
			scoreBoard.append(String.format("%-18s %2$d\n", name, player.getPlayerScore()));
		}
		return scoreBoard.toString();
	}

	private void printScoreBoard() {
		System.out.println(toString());

	}

	private Card[] validatePlay(Card[] cards) {
		Card previousCard = this.topCard;
		if (cards[0].getShape() == shape.WHOT || (this.topCard.getShape() == shape.WHOT && this.roundCount == 0))
			return null;
		else if (iNeed.size() > 0)
			if (cards[0].getShape() == iNeed.get(1)) {
				this.iNeed.clear();
				return null;
			} else
				return new Card[]{cards[0], null};
		else if (cards.length == 1 && pickTwo == 0)
			if (compareCardsByShape(cards[0], this.topCard))
				return null;
		int count = 0;
		for (Card card : cards) {
			if (!(compareCardsByNumber(previousCard, card)))
				return new Card[]{card, previousCard};
			previousCard = cards[count++];
		}

		return null;
	}

	private Card[] validate(int[] cardIndices) {
		Card[] cards = new Card[cardIndices.length];
		for (int i = 0; i < cards.length; ++i)
			cards[i] = this.playerTurn.hand.viewCardAt(cardIndices[i]);
		return this.validatePlay(cards);
	}

	private void processPlay() {
		String message = String.format("%s it's your turn to play.", this.playerTurn.getName());
		System.out.println(iNeed.size()); // todo: remove this later
		if (iNeed.size() > 0)
			message += String.format("\n%1$s needs a %2$s. Play a card of %2$s shape or go to market.", ((WhotPlayer) this.iNeed.get(0)).getName(), this.iNeed.get(1));
		if (pickTwo > 0)
			message += "\nYou have been asked to pick " + pickTwo + " .Enter m to yield or card index to defend(Value must be 2)";
		while (true) {
			int[] playerMove = this.playerTurn.playAsHuman(this.topCard, this.players.size(),
					this.market.getHandSize(), tenderEnabled, message);
			System.out.println(Arrays.toString(playerMove));//todo: testing
			if (playerMove[0] == -1) {
				if (pickTwo == 0) {
					System.out.println(this.playerTurn.getName() + " goes to market.");
					this.dealCurrentPlayerCard(1);
				} else {
					System.out.printf("%s picks %s", this.playerTurn.getName(), this.pickTwo);
					this.dealCurrentPlayerCard(pickTwo);
					pickTwo = 0;
				}
				return;
			} else {
				Card[] process = this.validate(playerMove);
				if (process == null) {
					Card playCard = null;
					for (int i : playerMove) {
						System.out.println("The current value is " + i);
						playCard = this.playerTurn.dealCard(i);
						this.addCardToDeckFromPlayer(playCard);
					}
					if (pickTwo > 0 && (playCard.getValue() != 2 || playCard.getValue() != 20)) {
						message = String.format("You can't block a pick to with a %s", playCard);
						continue;
					}
					switch (playCard.getValue()) {
						case 3:
						case 4:
						case 5:
						case 6:
						case 7:
						case 9:
						case 10:
						case 11:
						case 12:
						case 13:
							return;
						case 1:
							message = "Play your continue...";
							continue;
						case 2:
							WhotPlayer nextPlayer = this.players.get(this.players.size() % ++this.playerTurnIndex);
							this.dealPlayerCard(nextPlayer, 2 * playerMove.length);
							return;
						case 8:
							int playerIndex = this.playerTurnIndex;
							for (int i = 0; i < playerMove.length - 1; ++i) {
								this.updatePlayerTurn();
								if (this.playerTurnIndex == playerIndex)
									this.updatePlayerTurn();
								System.out.printf("%s suspension!!!", this.playerTurn.getName());
							}
							return;
						case 14:
							for (int i = 0; i < playerMove.length; i++) {//todo: add continue routine
								System.out.println("General Market everyone!!!");
								this.generalMarket();
							}
							return;
						case 20:
							System.out.println("What do you need");
							System.out.print("Box - b\nCircle - c\nCross - r\nStar - s\nTriangle - t\nEnter your response: ");
							char input = TextIO.getlnChar();
							while (!(input == 'b' || input == 'c' || input == 'r' || input == 's' || input == 't')) {
								System.out.println("Wrong input let's try that again.");
								System.out.print("Box - b\nCircle - c\nCross - r\nStar - s\nTriangle - t\nEnter your response: ");
								input = TextIO.getlnChar();
							}
							switch (input) {
								case 'b':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.BOX);
									break;
								case 'c':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.CIRCLE);
									break;
								case 'r':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.CROSS);
									break;
								case 's':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.STAR);
									break;
								case 't':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.TRIANGLE);
									break;
							}
							return;
					}
				} else {


					if (iNeed.isEmpty())
						message = String.format("You can't play a %1$s on a %2$s", process[0], process[1]);
					else {
						String name = this.playerTurn == iNeed.get(0) ? "you needed" : iNeed.get(0).toString() + "needs";
						message = String.format("You can't play a %1$s.\n%2$s a %3$s. Play a card of %3$s shape or" +
								" go to market.", process[0], name, this.iNeed.get(1));
					}
				}
			}
		}
	}

	public void startGame() {
		while (!this.gameOver) {
			processPlay();
			this.updatePlayerTurn();
			//continue;
		}
	}

	static class SortPlayersByScore implements Comparator<WhotPlayer> {

		@Override
		public int compare(WhotPlayer player1, WhotPlayer player2) {
			return player1.getPlayerScore() - player2.getPlayerScore();
		}

	}

}
