package com.rivancic.nfc;

import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import se.simbio.encryption.Encryption;

/**
 * <p>
 * Use library to encript and decript data.
 * <p/>
 * TODO implement async.
 * TODO secure credentials
 * Created by rivancic on 23/11/15.
 */
public class Security {

    private static Encryption encryption;

    static {
        byte[] iv = {-89, -19, 17, -82, 86, 11, -31, 30, -53, -111, 61, -7, -84, 95, 120, -53};
        try {
            encryption = new Encryption.Builder()
                    .setKeyLength(128)
                    .setKey("YourKey")
                    .setSalt("YourSalt")
                    .setIv(iv)
                    .setCharsetName("UTF8")
                    .setIterationCount(10)
                    .setDigestAlgorithm("SHA1")
                    .setBase64Mode(Base64.DEFAULT)
                    .setAlgorithm("AES/CBC/PKCS5Padding")
                    .setSecureRandomAlgorithm("SHA1PRNG")
                    .setSecretKeyType("PBKDF2WithHmacSHA1")
                    .build();
        } catch (NoSuchAlgorithmException e) {
            Log.e("Security", "Failed to initialize the algorithm", e);
        }
    }

    public static String encriptData(String rawData) {

        return encryption.encryptOrNull(rawData);
    }

    public static String decriptData(String encriptedData) {

        return encryption.decryptOrNull(encriptedData);
    }
}
