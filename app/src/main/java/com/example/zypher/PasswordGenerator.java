package com.example.zypher;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    // DEFINE THE COMBINATIONS TO USE FOR PASS GENERATOR
    private static final String Char_lowercaae = "abcdefghijklmnopqrstuvwxyz";
    private static final String Char_uppercase = Char_lowercaae.toUpperCase();
    private static final String Digit = "0123456789";
    private static final String otherPunctation = "!@#&()–[{}]:;',?/*";
    private static final String otherSymbol = "$^+=<>";
    private static final String otherSpecial = otherPunctation + otherSymbol;

    // DEFAULT LENGTH OF SLIDERS FOR PASSSWORD GENERATOR
    private static int lowCharlength = 4;
    private static int upCharlength = 4;
    private static int symbolLength = 4;
    private static int digitLength = 4;
    private static final int PASSLength = lowCharlength + upCharlength + symbolLength + digitLength;

    // RANDOMISE OBJECT FOR N INDEX (TOTAL SLIDER COUNT)
    private static final SecureRandom random = new SecureRandom ();

    // GET & SET METHODS
    public static int getLowCharlength() {
        return lowCharlength;
    }
    public static int getUpCharlength() {
        return upCharlength;
    }
    public static int getSymbolLength() {
        return symbolLength;
    }
    public static int getDigitLength() {
        return digitLength;
    }
    public static int getPASSLength() {
        return PASSLength;
    }
    public static void setLowCharlength(int length) {
        lowCharlength = length;
    }
    public static void setChar_uppercase(int length) {
        upCharlength = length;
    }
    public static void setDigitLength(int length) {
        digitLength = length;
    }
    public static void setSymbolLength(int length) {
        symbolLength = length;
    }

    // GENERATE STRING USING THE SLIDERS SET
    public static String generateRandomString ( String input, int size) {
        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Invalid Input");
        if (size < 1) throw new IllegalArgumentException("Invalid size");

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    // ADD RESULT OF RANDOMISER
    public static String generate() {
        StringBuilder result = new StringBuilder(PASSLength);

        // at least n lowercase characters
        String strLowerCase = generateRandomString(Char_lowercaae, getLowCharlength());
        result.append(strLowerCase);

        // at least n uppercase characters
        String strUpperCase = generateRandomString(Char_uppercase, getUpCharlength());
        result.append(strUpperCase);

        // at least n digits
        String strDigit = generateRandomString(Digit, getDigitLength());
        result.append(strDigit);

        // at least n special characters
        String strOtherSpecial = generateRandomString(otherSpecial, getSymbolLength());
        result.append(strOtherSpecial);

        // Combine all and shuffle
        String password = shufflestring(result.toString());

        return password;
    }

    // Join string from list
    private static String joinStringFromList (List<String> input) {
        if (input == null || input.size() <= 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <input.size(); i++){
            sb.append(input.get(i));
        }
        return sb.toString();
    }


    // SHUFFLE PASSWORD
    public static String shufflestring(String input) {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return joinStringFromList(result);
    }
}
