package cyph3r.myutils;

import java.util.Random;

public class StringUtil {
	static Random gen = new Random();
	static String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String digits = "0123456789";


	public static void main(String[] args) {
		for (char i = 0; i < 160; ++i) {

//			System.out.println((int) i + " -> " + i);
			System.out.println((StringUtil.getRandomLetter()));


		}
	}

	public static char getRandomLetter() {
		return (letters + digits).charAt(gen.nextInt(52));

	}

	public static String getRandomString(int length) {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < length; i++)
			string.append(getRandomLetter());

		return string.toString();
	}

}

