package cyph3r.game.whot;

import cyph3r.game.whot.Card.shape;
import cyph3r.myutils.StringUtil;
import textio.TextIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

//todo: Clean up all output.
//todo: Add last and semi card message
//fixme: Fix the multi-carding functionality.
//todo: add playerTurn variable as an int containing last card value
//todo: add contains method in Hand class

class WhotGame {
	private final ArrayList<WhotPlayer> players = new ArrayList<>();
	private final boolean tenderEnabled;
	private int pickTwo = 0;
	private Hand market = new Hand();
	private Hand deck = new Hand();
	private Card topCard;
	private WhotPlayer playerTurn;
	private int playerTurnIndex = 0;
	private int roundCount = 0;
	private final int numOfPacks;
	private ArrayList<Object> iNeed;
	private HashMap<String, Object> ineed;//mark: can I use this instead an ArrayList???


	WhotGame(int playerNum, int initCardNum, int numOfPacks, boolean tenderEnabled) {
		String welcomeMsg = ""; //todo: Fill in the game intro later
		String loadingCompleteMsg = ""; //todo: Fill this in later too
		System.out.println(welcomeMsg);
		if (numOfPacks <= 0 || (playerNum * initCardNum > (int) (0.75 * numOfPacks * 75))) // Ensures that the number of
			// cards to be distributed is not more than 75% of the total cards available
			throw new IllegalArgumentException("Not enough cards in market to share");
		if (numOfPacks > 4)
			throw new IllegalArgumentException("You are not allowed use more than 4 packs for a game");
		if (playerNum < 2)
			throw new IllegalArgumentException("Not enough players to start a game");
		this.tenderEnabled = tenderEnabled;
		iNeed = new ArrayList<>();
		this.numOfPacks = numOfPacks;
		this.populateMarket();
		for (int i = 0; i < playerNum; ++i) {
			System.out.print("Player " + (i + 1) + " enter your name: ");
			String name = TextIO.getlnWord();
			this.players.add(new WhotPlayer(StringUtil.toTitleCase(name)));

			for (int j = 0; j < initCardNum; ++j)
				dealPlayerCard(this.players.get(i), 1);
		}
		this.playerTurn = this.players.get(0);
		System.out.println(loadingCompleteMsg);
	}

	private void populateMarket() {
		for (int packNumber = 0; packNumber < numOfPacks; ++packNumber) {
			for (shape theShape : Card.shape.values()) {
				if (theShape == shape.WHOT)
					continue;
				for (int cardValue = 1; cardValue <= 14; ++cardValue) {
					this.market.receiveCard(new Card(theShape, cardValue));
				}
			}
			for (int whotNumber = 0; whotNumber < 5; ++whotNumber)
				this.market.receiveCard(new Card());
		}
		for (int i = 0; i < 2; ++i)
			this.market.shuffleHand();
		this.addCardToDeckFromMarket();
	}

	private void dealCurrentPlayerCard(int times) {
		this.dealPlayerCard(this.playerTurn, times);
	}

	private void dealPlayerCard(WhotPlayer player, int times) {
		for (int i = 0; i < times; ++i)
			player.receiveCard(this.market.dealCard());
	}

	private void addCardToDeckFromMarket() {
		Card card = this.market.dealCard();
		this.topCard = card;
		this.deck.receiveCard(card);
	}

	private void resetDeck() {//todo: ensure this works as is supposed to
		Card[] underDeck = this.deck.dealCards(0, this.deck.getHandSize() - 2);
		if (underDeck.length == 0) {
			System.out.println("There are no more cards left in the market.");
			gameOver();
		}
		this.market.receiveCards(underDeck);
		this.market.shuffleHand();
	}

	private void addCardsToDeckFromPlayer() {
		this.deck.receiveCards(this.playerTurn.flushOutgoingCards());
		this.topCard = deck.peek(this.deck.getHandSize() - 1);
	}

	private void generalMarket() {
		for (WhotPlayer player : this.players) {
			if (player == this.playerTurn)
				continue;
			System.out.println(String.format("%s goes to market.", player.getName()));
			this.dealPlayerCard(player, 1);
		}
	}

	private void updatePlayerTurn() {
		if (this.playerTurn.handEmpty() || this.market.isEmpty() && tenderEnabled)
			this.gameOver();
		else if (!tenderEnabled)
			this.resetDeck();
		else {
			++this.playerTurnIndex;
			this.roundCount++;
			if (this.playerTurnIndex >= this.players.size()) {
				this.playerTurnIndex = 0;
			}
			this.playerTurn = this.players.get(playerTurnIndex);
		}
	}

	public String toString() {
		ArrayList<WhotPlayer> players_copy = new ArrayList<>(this.players);
		players_copy.sort(Comparator.comparingInt(WhotPlayer::getPlayerScore).reversed());
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
		if (cards[0].getShape() == shape.WHOT || (this.topCard.getShape() == shape.WHOT && this.roundCount == 0)) {
			this.pickTwo = 0;
			return null;
		} else if (iNeed.size() > 0)
			if (cards[0].getShape() == iNeed.get(1)) {
				this.iNeed.clear();
				return null;
			} else
				return new Card[]{cards[0], null};
		else if (cards.length == 1 && pickTwo == 0)
			if (Card.compareCardsByShape(cards[0], this.topCard))
				return null;
		int count = 0;
		for (Card card : cards) {
			if (Card.compareCardsByNumber(previousCard, card) || (count == 0 && Card.compareCardsByShape(previousCard, card))) {
				previousCard = cards[count++];
				continue;
			}
			return new Card[]{card, previousCard};
		}

		return null;
	}

	private void processPlay() {
		StringBuilder message = new StringBuilder(String.format("%s it's your turn to play.\n", this.playerTurn.getName()));
		if (Tester.testing)
			System.out.println(iNeed.size()); // todo: testing
		if (iNeed.size() > 0) {
			StringBuilder name = this.playerTurn == iNeed.get(0) ? new StringBuilder("You needed ") : new StringBuilder(iNeed.get(0).toString() + " needs ");
			message.append(name.append(this.iNeed.get(1)).toString());
		}
		if (pickTwo > 0)
			message.append("\nYou have been asked to pick ").append(pickTwo).append(". Enter m to yield or card index to defend(Value must be 2 or WHOT)");
		while (true) {
			Card[] cardToPlay = this.playerTurn.playAsHuman(this.topCard, this.market.getHandSize(), message.toString());
			if (Tester.testing)
				System.out.println(Arrays.toString(cardToPlay));//todo: testing
			if (cardToPlay[0] == null) {
				if (pickTwo == 0) {
					System.out.println(this.playerTurn.getName() + " goes to market.");
					this.dealCurrentPlayerCard(1);
				} else {
					System.out.printf("%s picks %s\n", this.playerTurn.getName(), this.pickTwo);
					this.dealCurrentPlayerCard(pickTwo);
					pickTwo = 0;
				}
				System.out.println(); //mark: Newline
				return;
			} else {
				Card[] process = this.validatePlay(cardToPlay);
				if (process == null) {
					this.addCardsToDeckFromPlayer();
					int cardValue = cardToPlay[0].getValue();

					if (pickTwo > 0 && cardValue != 2 && cardValue != 20) {
						message.delete(0, message.length());
						message = new StringBuilder(String.format("You can't block a pick to with a %s", cardValue));
						continue;
					}
					switch (cardValue) {
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
							message.delete(0, message.length());
							message = new StringBuilder("Play your continue...");
							continue;
						case 2:
							pickTwo += 2 * cardToPlay.length;
							return;
						case 8:
							for (int i = 0; i < cardToPlay.length - 1; ++i) {
								this.updatePlayerTurn();
								System.out.printf("%s suspension!!!", this.playerTurn.getName());
							}
							message.delete(0, message.length());
							message = new StringBuilder("Play your continue");
							continue;
						case 14:
							for (int i = 0; i < cardToPlay.length; i++) {
								System.out.println("General Market everyone!!!");
								this.generalMarket();
							}
							message.delete(0, message.length());
							message = new StringBuilder("Play your continue");
							continue;
						case 20:
							System.out.println("Your cards are:");
							System.out.println(this.playerTurn.hand);
							System.out.println("What do you need...");
							System.out.print("Box - b\nCircle - c\nCross - r\nStar - s\nTriangle - t\nEnter your response: ");
							char input = Character.toLowerCase(TextIO.getlnChar());
							while (input != 'b' && input != 'c' && input != 'r' && input != 's' && input != 't' && input != '1' && input != '2' && input != '3' && input != '4' && input != '5') {
								System.out.println("Wrong input let's try that again.");
								System.out.print("Box - b\nCircle - c\nCross - r\nStar - s\nTriangle - t\nEnter your response: ");
								input = Character.toLowerCase(TextIO.getlnChar());
							}
							switch (input) {
								case '1':
								case 'b':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.BOX);
									break;
								case '2':
								case 'c':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.CIRCLE);
									break;
								case '3':
								case 'r':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.CROSS);
									break;
								case '4':
								case 's':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.STAR);
									break;
								case '5':
								case 't':
									iNeed.add(this.playerTurn);
									iNeed.add(shape.TRIANGLE);
									break;
							}
							System.out.println();
							return;
					}
				} else {

					message.delete(0, message.length());
					if (iNeed.isEmpty())
						message = new StringBuilder(String.format("You can't play a %1$s on a %2$s", process[0], process[1]));
					else {
						String name = this.playerTurn == iNeed.get(0) ? "you needed" : iNeed.get(0).toString() + " needs";
						message = new StringBuilder(String.format("You can't play a %1$s.\n%2$s a %3$s. Play a card of %3$s shape or go to market.", process[0], name, this.iNeed.get(1)));
					}
				}
			}
		}
	}

	private void resetGame() {
		//todo; implement this later
	}

	private void gameOver() {
		ArrayList<WhotPlayer> playersCopy = new ArrayList<>(this.players);
		if (tenderEnabled) {
			System.out.println("GAME OVER, TENDER!!!");
			playersCopy.sort(Comparator.comparingInt(WhotPlayer::getPlayerCardWeight));
			int lastWeight = playersCopy.get(0).getPlayerCardWeight();
			int position = 0;
			boolean draw = false;
			for (WhotPlayer player : playersCopy) {
				if (player.getPlayerCardWeight() != lastWeight) {
					draw = false;
					position++;
				} else {
					if (position != 0)
						draw = true;
				}
				if (position == 0)
					position++;
				lastWeight = player.getPlayerCardWeight();
				String positionString = position == 1 ? "1st" : position == 2 ? "2nd" : position == 3 ? "3rd" : position + "th";
				int score = this.players.size() - position + 1;//So that players.size() - 1st + 1 ==  players.size
				player.updatePlayerScore(score);
				if (!draw)
					System.out.printf("%s comes in at %s position.\n", player.getName(), positionString);
				else
					System.out.printf("%s comes draws at %s position.\n", player.getName(), positionString);

			}

		} else {
			System.out.printf("%s wins this round.\n", this.playerTurn.getName());
			this.playerTurn.updatePlayerScore(4);

		}
		this.printScoreBoard();
		System.out.print("Would you like to play a new game(y/n): ");
		String response = TextIO.getWord();
		if (response.equalsIgnoreCase("n"))
			System.exit(0);
		else
			this.resetGame();
	}

	void startGame() {
		while (true) {
			processPlay();
			this.updatePlayerTurn();
		}
	}

}