package cyph3r.game.whot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Hand {
	private ArrayList<Card> hand = new ArrayList<>();
	private int cardWeight = 0;

	Hand() {

	}

	Hand(Card card) {

		receiveCard(card);
	}

	Hand(Card[] cards) {
		for (Card card : cards) {
			receiveCard(card);
		}
	}

	int getHandSize() {
		return this.hand.size();
	}

	void clearCards() {
		this.hand.clear();
	}

	boolean isEmpty() {
		return this.getHandSize() == 0;
	}

	int getCardWeight() {
		return this.cardWeight;
	}

	Card dealCard() {
		Card toRemove = this.hand.remove(this.hand.size() - 1);
		this.cardWeight -= toRemove.getValue();
		return toRemove;
	}

	Card[] dealCards(int... indices) {
		Card[] toReturn = new Card[indices.length];
		for (int i = 0; i < indices.length; ++i)
			toReturn[i] = dealCard(indices[i]);
		return toReturn;
	}

	Card[] dealCards(int start, int end) {
		if (this.isEmpty())
			return new Card[]{};
		if (start < 0 || end > this.getHandSize())
			throw new IllegalArgumentException("Hand is greater than or lass than inputted range");
		return this.hand.subList(start, end).toArray(new Card[]{});
	}

	Card dealCard(int ordinal) {
		Card toRemove = this.hand.remove(ordinal);
		this.cardWeight -= toRemove.getValue();
		return toRemove;
	}

	Card dealCard(Card card) {
		return this.dealCards(new Card[]{card})[0];
	}

	Card[] dealCards(Card[] cards) {
		Card[] toReturn = new Card[cards.length];
		int count = 0;
		for (Card card : cards) {
			int cardIndex = this.hand.indexOf(card);
			if (cardIndex == -1) continue;
			toReturn[count] = this.hand.remove(cardIndex);
		}
		return toReturn;
	}

	void receiveCard(Card card) {
		this.cardWeight += card.getValue();
		this.hand.add(card);
	}


	void receiveCards(Card[] cards) {
		for (Card card : cards)
			this.receiveCard(card);
	}

	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		Card[] cards = this.getHandAsArray();
		for (int i = 1; i < cards.length + 1; i++) {
			toReturn.append(String.format("%1$-30s", ("[" + i + "]" + (cards[i - 1].toString()))));
			if (i % 2 == 0 && i != cards.length)
				toReturn.append("\n");
		}
		// if (cards.length % 2 == 1)
		// toReturn.append("\n");

		return toReturn.toString();
	}

	public String getHandAsString() {
		return this.hand.toString();
	}

	Card[] getHandAsArray() {
		return this.hand.toArray(new Card[0]);
	}

	Card peek(int index) {
		try {
			return this.hand.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}


	void sortByValue() {
		this.hand.sort(new SortCardByValue());
	}

	void sortByShape() {
		this.hand.sort(new SortCardByShape());
	}

	void shuffleHand() {
		Collections.shuffle(this.hand);
	}

	static class SortCardByValue implements Comparator<Card> {

		@Override
		public int compare(Card card1, Card card2) {
			return card1.getValue() - card2.getValue();
		}

	}


	static class SortCardByShape implements Comparator<Card> {

		@Override
		public int compare(Card card1, Card card2) {
			return card1.getShapeAsEnum().ordinal() - card2.getShapeAsEnum().ordinal();
		}

	}


}
