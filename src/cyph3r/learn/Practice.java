package cyph3r.learn;

import java.util.ArrayList;

public class Practice implements Runnable {

	public static void main(String[] args) {
		Practice ke = new Practice();
		ArrayList<Card> list = new ArrayList<>();
//		list.stream().
//		System.out.println(ke.add(2,3));
//		while(true) {
//			char f = TextIO.getlnChar();
//			System.out.println(f !='d' || f!='z');
//			break;
//		}
//		for(int i = 0;i < 5;++i)
//			System.out.println("Hi player "+i );
//
//			Integer[] t = new Integer[] {};
		String er = "d";
		String t[] = er.split("ddddddddd");
		System.out.println("'" + t[0] + "'");

	}


	public static int adder(Practice function, int num_a, int num_b) {
		return function.add(num_a, num_b);

	}


	@Override
	public int add(int a, int b) {
		return a + b;
	}

}