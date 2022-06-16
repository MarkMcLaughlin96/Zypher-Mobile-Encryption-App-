package com.example.zypher;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


// CRYPTO UTILS UTILITY CLASS APDAPTED FROM @author www.codejava.net
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        System.gc();
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        System.gc();
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int ciperMode, String key, File inputFile, File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(ciperMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);

            CipherOutputStream outputStream = new CipherOutputStream(new FileOutputStream(outputFile), cipher);

            byte[] buffer = new byte[8192];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }

            inputStream.close();
            outputStream.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |InvalidKeyException | IOException | OutOfMemoryError ex) {
            throw new CryptoException("Could not complete operation", ex);
        }
    }
}
