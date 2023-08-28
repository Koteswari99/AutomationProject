package com.capco.engine;



public class Mainfunction {

	public static boolean isInt(String input) {
		try {
			Integer.parseInt(input);
			System.out.println("valid");
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isString(String input) {
		return input.matches("[a-zA-Z0-9]+");
	}

	public static boolean isBoolean(String input) {
		return input.equals("true") || input.equals("false");
	}

	public void function1(String string1, int int1, int int2) {

		switch (string1) {
		case "Sumtwovalues":
			int c = int1 + int2;
			System.out.println("the sum is :" + c);
			

		default:
			break;

		}
	}
	public void function2(String string1, String str1, String str2) {

		switch (string1) {
		case "Comparetwostrings":
			
			if (str1.equalsIgnoreCase(str2)) {
	            System.out.println("The two strings are equal.");
	        } else {
	            System.out.println("The two strings are not equal.");
	        }

		default:
			break;

		}
	}

}