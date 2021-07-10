package cyph3r.game.whot;


public class Tester {


	static boolean testing = false;

	public static void main(String[] args) {
		WhotGame whot = new WhotGame(3, 5, 1, true);
//		System.out.println(whot);
		whot.startGame();

//		Card[] numbers = new Card[]{null};
//		System.out.println(numbers[0]);

//		Hand hand = new Hand(new Card[]{new Card(), new Card(Card.shape.BOX, 3), new Card(Card.shape.CROSS, 7), new Card(Card.shape.TRIANGLE, 8)});
//		ArrayList<Integer> indexToReturn = new ArrayList<>();
//		indexToReturn.add(0);
//		indexToReturn.add(2);
//		Card[] firb = indexToReturn.stream().map((a) -> hand.peek(a)).toArray(Card[]::new);
//		System.out.println(Arrays.toString(firb));


//		ArrayList<Integer>  dd= new ArrayList<>();
//		dd.add(23);
//		dd.add(13);
//		for (int i = 0; i < 20; i++) {
//			dd.add(i);
//		}
//
//		dd = dd.stream().collect(ArrayList::new,(a,b)->a.add(b+9), ArrayList::addAll);
//		System.out.println(dd);
	}


}
