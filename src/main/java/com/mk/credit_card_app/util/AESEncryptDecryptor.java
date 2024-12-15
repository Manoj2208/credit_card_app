package com.mk.credit_card_app.util;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESEncryptDecryptor {

    private static final String SECRET_KEY = "1234567890123456";

    public static String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Encryption algorithm/padding is not available", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid encryption key. Key must be 16, 24, or 32 bytes", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Encryption error: Invalid block size or padding", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Decryption algorithm/padding is not available", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid decryption key. Key must match encryption key", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 input", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Decryption error: Data may be corrupted or key is incorrect", e);
        }
    }
}

