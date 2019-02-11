package net.nersa.lobby.utils;

public class StringUtils {

	public static String capitalization(String input){
		if (input == null || input.length() == 0) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
    public static String toString(final Object object) {
        return String.valueOf(object);
    }
}
