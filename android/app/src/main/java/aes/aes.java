package aes;


import android.os.Build;

import java.nio.ByteBuffer;
import at.favre.lib.crypto.HKDF;
import java.security.*;
import java.util.*;
import java.util.stream.IntStream;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class aes {
    private static byte[] decrypt(String key, String cipherMessage) throws Exception {
        byte[] cipherMessageByte = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cipherMessageByte = Base64.getDecoder().decode(cipherMessage);
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessageByte);

        int ivLength = (byteBuffer.get());
        if (ivLength != 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);

        int macLength = (byteBuffer.get());
        if (macLength != 32) { // check input parameter
            throw new IllegalArgumentException("invalid mac length");
        }
        byte[] mac = new byte[macLength];
        byteBuffer.get(mac);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        String salt = key.substring(0, key.length()/4) + key.substring(0, key.length()/4);
        key = key + salt;

        byte[] encKey = HKDF.fromHmacSha256().expand(key.getBytes(), "encKey".getBytes(), 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(key.getBytes(), "authKey".getBytes(), 32);

        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(iv);
        hmac.update(cipherText);

        byte[] refMac = hmac.doFinal();

        if (!MessageDigest.isEqual(refMac, mac)) {
            throw new SecurityException("could not authenticate");
        }

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(iv));
        byte[] plainText = cipher.doFinal(cipherText);

        return plainText;
    }
    private static String convertToBase64(int[] ints) {
        ByteBuffer buf = ByteBuffer.allocate(ints.length);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IntStream.of(ints).forEach(i -> buf.put((byte)i));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(buf.array());
        }
        return null;
    }
    public static String aesDecryption(String base64File, String key) throws Exception {
        byte[] decrypted = decrypt(key, base64File);

        return new String(decrypted);
    }
}
