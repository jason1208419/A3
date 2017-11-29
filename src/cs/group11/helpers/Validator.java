package cs.group11.helpers;

public class Validator {

	private Validator() {
	}// Make Validator uninstantiatable.

	public static boolean isStringEmpty(String s) {
		return isNull(s) || s.trim().isEmpty();
	}

	public static boolean isNull(Object s) {
		return s == null;
	}

	public static boolean isIntValid(int value, int upperBound, int lowerBound) {
		return value <= upperBound && value >= lowerBound;
	}

}
