package mhealth.mvax.records.utilities;

import java.security.SecureRandom;
import java.util.Random;

/**
 * The goal of this class is to be able to randomly generate alphanumeric string which would allow for unique storage
 * keys/headers for Firebase data entries.
 * */

public class RandomStringGenerator {

    //Code modified from Stack overflow: https://stackoverflow.com/questions/18069434/generating-alphanumeric-random-string-in-java
    public static String randomString(int length) {
        char[] characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }
}
