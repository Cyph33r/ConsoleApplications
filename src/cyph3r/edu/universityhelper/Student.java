package cyph3r.edu.universityhelper;

import cyph3r.myutils.StringUtil;

import java.util.Date;
import java.util.Random;


public class Student {

	private String name;
	private Date DOB;


	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(StringUtil.getRandomString(8));
		}

	}

	public static char getRandomChar() {
		Random gen = new Random();
		return ' ';

	}

	public static String wordGrid(int rows, int columns) {

		return " ";
	}
}