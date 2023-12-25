package io.github.antonyhaman.podegen.core.utils;

public class StringUtils {

    public static String capitalize(String str) {
        int strLen = str != null ? str.length() : 0;
        if (strLen == 0) {
            return str;
        } else {
            int firstCodepoint = str.codePointAt(0);
            int newCodePoint = Character.toTitleCase(firstCodepoint);
            if (firstCodepoint == newCodePoint) {
                return str;
            } else {
                int[] newCodePoints = new int[strLen];
                int outOffset = 0;
                newCodePoints[outOffset++] = newCodePoint;

                int codepoint;
                for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; inOffset += Character.charCount(codepoint)) {
                    codepoint = str.codePointAt(inOffset);
                    newCodePoints[outOffset++] = codepoint;
                }

                return new String(newCodePoints, 0, outOffset);
            }
        }
    }
}
