package cyph3r.myutils;

import java.util.Random;

public class StringUtil {
	final static String asciiUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final static String whitespace = " \t\n\r\f";
	final static String digits = "0123456789";
	final static String hexdigits = digits + "abcdef" + "ABCDEF";
	final static String octdigits = "01234567";
	final static String asciiLowercase = "abcdefghijklmnopqrstuvwxyz";
	final static String punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
	final static String asciiLetters = asciiLowercase + asciiUppercase;
	final static String printable = digits + asciiLetters + punctuation + whitespace;
	final static Random gen = new Random();

	public static void main(String[] args) {
		System.out.println(toTitlecase("fg wh thr why"));
//		System.out.println(Character.toTitleCase('s'));
	}

	public static String toTitlecase(String word) {
		StringBuilder toReturn = new StringBuilder();
		boolean seenWhitespace = true;
		for (int i = 0; i < word.length(); i++) {
			char character = word.charAt(i);
			if (!(isWhitespace(character))) {
				if (seenWhitespace)
					toReturn.append(Character.toTitleCase(character));
				else
					toReturn.append(character);
				seenWhitespace = false;

				continue;
			}

			seenWhitespace = true;
			toReturn.append(character);
		}
		return toReturn.toString();
	}

	public static boolean isWhitespace(String sequence) {
		if (sequence.isEmpty())
			return false;
		for (int i = 0; i < sequence.length(); i++) {
			char character = sequence.charAt(i);
			for (int j = 0; j < whitespace.length(); j++)
				if (!(Character.isWhitespace(character)))
					return false;
		}
		return true;


	}

	public static boolean isWhitespace(char character) {
		return isWhitespace(Character.toString(character));
	}

	public static char getRandomLetterAsChar() {
		return (asciiLetters + digits).charAt(gen.nextInt(52));

	}

	public static String getRandomLetterAsString() {
		return Character.toString(getRandomLetterAsChar());
	}

	public static String getRandomString(int length) {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < length; i++)
			string.append(getRandomLetterAsChar());
		return string.toString();
	}


}

