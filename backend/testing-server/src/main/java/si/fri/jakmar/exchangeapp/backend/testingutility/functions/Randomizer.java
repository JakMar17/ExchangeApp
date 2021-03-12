package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import java.util.stream.Stream;

public abstract class Randomizer {

    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int charsLength = chars.length();


    /**
     * creates random string of length 8, where string is not equal to any of elements in stream notequal
     * @param notEqual created string cannot be equal to any of its element
     * @return
     */
    public static String createRandomString(Stream<String> notEqual) {
        return createRandomStringWithLength(notEqual, 8);
    }

    /**
     * creates random string of given length, where string is not equal to any of elements in stream notequal
     * @param notEqual created string cannot be equal to any of its element
     * @param length of wanted generated string
     * @return
     */
    public static String createRandomStringWithLength(Stream<String> notEqual, int length) {
        var string = random(length);
        return notEqual.filter(string::equals).findAny().isEmpty()
                ? string
                : createRandomStringWithLength(notEqual, length);
    }

    /**
     * creates random string of length length
     * @param length
     * @return
     */
    private static String random(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * charsLength);
            stringBuilder.append(chars.charAt(rand));
        }

        return stringBuilder.toString();
    }
}
