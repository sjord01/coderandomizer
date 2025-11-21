package coderandomizer.util;

import java.security.SecureRandom;

public class RandomCodeGenerator {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "123456789";
    private static final String SYMBOLS = "!@#$%&*.-_";
    private static final String ALPHANUM = LETTERS + DIGITS;
    private static final SecureRandom rnd = new SecureRandom();

    /**
     * Generate a 6-character code using lower-case letters and digits 1-9.
     * Randomly sometimes insert symbols at positions with a small probability.
     */
    public static String generate() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            double r = rnd.nextDouble();
            if (r < 0.15) { // 15% chance to be a symbol at this position
                sb.append(SYMBOLS.charAt(rnd.nextInt(SYMBOLS.length())));
            } else {
                sb.append(ALPHANUM.charAt(rnd.nextInt(ALPHANUM.length())));
            }
        }
        return sb.toString();
    }

    public static String cleanup(String code) {
        if (code == null) return null;
        return code.replaceAll("[^a-z1-9]", "x");
    }

    public static boolean isGood(String code) {
        return code != null && code.matches("^[a-z1-9]{6}$");
    }
}
