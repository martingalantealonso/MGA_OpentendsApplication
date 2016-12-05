package com.galante.martin.opentendsapplication.data;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Martin on 05/12/2016.
 *
 */

public class GlobalData {
    public static String ts = System.currentTimeMillis() + "";
    public static String public_key = "cd31f94797faaf1c26a65f7a20cb086b";
    public static String private_key = "33ae0776e56c8c24f8edf1f315c8c2e4dc68f609";
    public static String hash = getMD5(ts + private_key + public_key);

    private static String getMD5(String input) {
        try {
            Log.i("string = ", input);
            MessageDigest mMessageDigest = MessageDigest.getInstance("MD5");
            byte[] messageDigest = mMessageDigest.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
