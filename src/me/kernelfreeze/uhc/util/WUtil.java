package me.kernelfreeze.uhc.util;

public class WUtil
{
    public static String capitalizeFully(String lowerCase) {
        lowerCase = lowerCase.toLowerCase();
        return capitalize(lowerCase);
    }
    
    public static String capitalize(final String s) {
        final char[] charArray = s.toCharArray();
        int n = 1;
        for (int i = 0; i < charArray.length; ++i) {
            final char c = charArray[i];
            if (isDelimiter(c, null)) {
                n = 1;
            }
            else if (n != 0) {
                charArray[i] = Character.toTitleCase(c);
                n = 0;
            }
        }
        return new String(charArray);
    }
    
    private static boolean isDelimiter(final char c, final char[] array) {
        if (array == null) {
            return Character.isWhitespace(c);
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (c == array[i]) {
                return true;
            }
        }
        return false;
    }
}
