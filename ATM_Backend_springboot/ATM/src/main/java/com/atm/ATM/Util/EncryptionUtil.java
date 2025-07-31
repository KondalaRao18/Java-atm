package com.atm.ATM.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    @Value("${app.encryption.key:mySecretKey12345}")
    private String encryptionKey;

    private SecretKey getSecretKey() {
        // Ensure key is 16 bytes for AES-128
        byte[] key = encryptionKey.getBytes();
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while encrypting data", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while decrypting data", e);
        }
    }

    public String encryptBalance(double balance) {
        return encrypt(String.valueOf(balance));
    }

    public double decryptBalance(String encryptedBalance) {
        if (encryptedBalance == null || encryptedBalance.isEmpty()) {
            return 0.0;
        }
        String decryptedBalance = decrypt(encryptedBalance);
        return Double.parseDouble(decryptedBalance);
    }
}