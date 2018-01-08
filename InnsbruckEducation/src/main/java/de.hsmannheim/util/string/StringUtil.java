package de.hsmannheim.util.string;

public class StringUtil {

    public static int countOccurrencesOfCharInString(String text, char letter) {
        int amount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                amount++;
            }
        }
        return amount;
    }

    public static String getLongestStringFromArray(String[] strings) {
        String longestString = strings[0];
        for (String string : strings) {
            if (string.length() > longestString.length()) {
                longestString = string;
            }
        }
        return longestString;
    }
}
