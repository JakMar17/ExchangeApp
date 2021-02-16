package si.fri.jakmar.backend.exchangeapp.functions;

import java.util.stream.Stream;

public abstract class RandomizerService {
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int charsLength = chars.length();


    public static String createRandomString(Stream<String> notEqual) {
        return createRandomStringWithLength(notEqual, 8);
    }

    public static String createRandomStringWithLength(Stream<String> notEqual, int length) {
        var string = random(length);
        return notEqual.filter(e -> e.equals(string)).findAny().isEmpty()
                ? string
                : createRandomStringWithLength(notEqual, length);
    }

    private static String random(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * charsLength);
            stringBuilder.append(chars.charAt(rand));
        }

        return stringBuilder.toString();
    }
}
