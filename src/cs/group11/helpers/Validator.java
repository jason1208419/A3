package cs.group11.helpers;

public abstract class Validator {

    public static boolean isStringEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
