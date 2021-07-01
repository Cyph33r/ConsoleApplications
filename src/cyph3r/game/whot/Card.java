package cyph3r.game.whot;

public class Card {
	private final shape cardShape;
	private final int cardValue;

	public Card() {
		this.cardShape = Card.shape.WHOT;
		this.cardValue = 20;
	}

	public Card(shape cardShape, int cardValue) {
		this.cardShape = cardShape;
		if (cardShape == Card.shape.WHOT)
			this.cardValue = 20;
		else if (cardValue > 0 && cardValue < 15)
			this.cardValue = cardValue;
		else
			throw new IllegalArgumentException("Invalid card value entered: " + cardValue);
	}

	public static boolean compareCardsByNumber(Card card1, Card card2) {
		return card1.getValue() == card2.getValue();

	}

	public static boolean compareCardsByShape(Card card1, Card card2) {
		return card1.getShape() == card2.getShape();
	}

	public shape getShapeAsEnum() {
		return this.cardShape;
	}

	public String getShapeAsString() {
		return this.cardShape.toString();
	}

	public int getValue() {
		return this.cardValue;
	}

	public shape getShape() {
		return this.cardShape;
	}

	public String toString() {
		if (this.cardShape == Card.shape.WHOT)
			return this.cardShape.toString() + " " + this.cardValue;
		else {
			StringBuilder toReturn = new StringBuilder();

			switch (this.cardValue) {
				case 1:
					toReturn.append("One");
					break;
				case 2:
					toReturn.append("Two");
					break;
				case 3:
					toReturn.append("Three");
					break;
				case 4:
					toReturn.append("Four");
					break;
				case 5:
					toReturn.append("Five");
					break;
				case 6:
					toReturn.append("Six");
					break;
				case 7:
					toReturn.append("Seven");
					break;
				case 8:
					toReturn.append("Eight");
					break;
				case 9:
					toReturn.append("Nine");
					break;
				case 10:
					toReturn.append("Ten");
					break;
				case 11:
					toReturn.append("Eleven");
					break;
				case 12:
					toReturn.append("Twelve");
					break;
				case 13:
					toReturn.append("Thirteen");
					break;
				default:
					toReturn.append("Fourteen");
					break;

			}
			toReturn.append(" of ").append(this.cardShape.toString());
			return toReturn.toString();

		}
	}

	enum shape {
		BOX, CIRCLE, CROSS, STAR, TRIANGLE, WHOT
	}

}
