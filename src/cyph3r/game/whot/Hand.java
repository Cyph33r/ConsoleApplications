package cyph3r.game.whot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
	private ArrayList<Card> hand = new ArrayList<>();
	private int cardWeight = 0;

	public Hand() {

	}

	public Hand(Card card) {

		receiveCard(card);
	}

	public Hand(Card[] cards) {
		for (Card card : cards) {
			receiveCard(card);
		}
	}

	public int getHandSize() {
		return this.hand.size();
	}

	public void flushCards() {
		this.hand.clear();
	}

	public boolean isEmpty() {
		return this.getHandSize() == 0;
	}

	public int getCardWeight() {
		return this.cardWeight;
	}

	public Card playCard() {
		Card toRemove = this.hand.remove(this.hand.size() - 1);
		this.cardWeight -= toRemove.getValue();
		return toRemove;

	}

	public Card[] playCards(int... indices) {
		Card[] toReturn = new Card[indices.length];
		for (int i = 0; i < indices.length; ++i)
			toReturn[i] = playCard(indices[i]);
		return toReturn;
	}

	public Card[] playCards(int start, int end) {
		if (this.isEmpty())
			return new Card[]{};
		if (start < 0 || end > this.getHandSize())
			throw new IllegalArgumentException("Hand is greater than or lass than inputted range");
		return this.hand.subList(start, end).toArray(new Card[]{});
	}

	public Card playCard(int ordinal) {
		Card toRemove = this.hand.remove(ordinal);
		this.cardWeight -= toRemove.getValue();
		return toRemove;
	}

	public void receiveCard(Card card) {
		this.cardWeight += card.getValue();
		this.hand.add(card);
	}


	public void receiveCards(Card[] cards) {
		for (Card card : cards) {
			this.receiveCard(card);
		}
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

	public Card[] getHandAsArray() {
		return this.hand.toArray(new Card[0]);
	}

	public Card viewCardAt(int index) {
		return this.hand.get(index);
	}

	public void sortByValue() {
		this.hand.sort(new SortCardByValue());
	}

	public void sortByShape() {
		this.hand.sort(new SortCardByShape());
	}

	public void shuffleHand() {
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
