package org.gp3.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    /**
     * Возвращает строковое представление хэша SHA-256 для заданной строки.
     * Хэш предваряется "db" и обрезается до первых 30 символов, если его длина превышает 30 символов.
     *
     * @param input строка, для которой необходимо вычислить хэш
     * @return строка, представляющая собой хэш SHA-256, предварённая "db"
     * @throws RuntimeException если алгоритм SHA-256 недоступен
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String fullHex = hexString.toString();
            String hashString = "db" + 
            (fullHex.length() > 30 ? fullHex.substring(0, 30) : fullHex);
            return hashString;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

