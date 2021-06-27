package cyph3r.learn;

import cyph3r.learn.Card.shape;

import java.util.Arrays;
import java.util.Random;

public class Practice2 {

	public static void main(String[] args) {
		while (true) {
			Random gen = new Random();
			// GetFunc myFunction = getFunc("sqrt");
			// out.print(myFunction.func(37789,1));
//			System.out.printf("Sorry wrong input, ensure all index(dices) are in range of (1 - %1$d)\n", 34);

			WhotPlayer me = new WhotPlayer("Tade");
			for (int i = 0; i < 20; ++i)
				me.receiveCard(new Card(shape.values()[gen.nextInt(6)], gen.nextInt(14) + 1));
			System.out.println(Arrays.asList(me.playAsHuman(new Card(shape.STAR, 12), 3, 40, true, "Play Card Joooor!!")));
			TextIO.getln();

		}
	}

	public static GetFunc getFunc(String funcName) {
		// assert funcName == "jth":"ARRRRRRRGH!!!!!";
		switch (funcName) {
			case "add":
				return Integer::sum;
			case "min":
				return (a, b) -> a - b;
			case "div":
				return (a, b) -> a / b;
			case "mult":
				return (a, b) -> a * b;
			case "sqrt":
				return Math::subtractExact;
			default:
				return Integer::sum;

		}

	}

}
