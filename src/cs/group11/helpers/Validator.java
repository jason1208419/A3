package cs.group11.helpers;

import java.io.File;

public final class Validator {

	private Validator() {// Make Validator uninstantiatable.
	}

	public static boolean isStringEmpty(String s) {
		return isNull(s) || s.trim().isEmpty();
	}

	public static boolean isNull(Object s) {
		return s == null;
	}

	public static boolean isFileValid(File f) {
		return (!isNull(f) && f.exists() && f.canRead());
	}

	public static boolean isIntValid(int value, int upperBound, int lowerBound) {
		return value <= upperBound && value >= lowerBound;
	}

	public static boolean isPositive(double value) {
		return value >= 0D;
	}

	public static boolean isNegative(double value) {
		return value < 0D;
	}

}
