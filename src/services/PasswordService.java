package services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


/*
*********************************************************************************
* Все методы для хэширования пароля (кроме verifyPassword()) были взяты с гугла *
*********************************************************************************
* */


public class PasswordService {
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean verifyPassword(String password, byte[] salt, String expectedHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || salt == null || expectedHash == null)
            return false;
        String passwordHash = hashPassword(password, salt);
        return passwordHash.equals(expectedHash);
    }
}
