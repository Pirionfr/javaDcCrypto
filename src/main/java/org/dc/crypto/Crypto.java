package org.dc.crypto;


import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang.ArrayUtils;
import org.dc.exception.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crypto{
    private static final int IV_SIZE = 16;
    private static final String CIPHER_ALGORITHM = "AES/CFB128/NoPadding";
    private static final String KEY_ALGORITHM = "AES";

    //base64 pattern RFC4648
    private static Pattern pattern = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    private String masterKey;
    private byte[] key;
    private SecretKeySpec secretKeySpec;

    private Cipher cipher;

    /**
     * constructor
     * @param key master key
     */
    public Crypto( String key) {
        this.masterKey = key;
    }

    public void init() throws CryptoException {
        try {
            this.key = hashTo32Bytes(this.masterKey );
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("error while hash key ", e);
        }
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException|NoSuchPaddingException e) {
            throw new CryptoException("error while init cypher Algorithm", e);
        }
        secretKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
    }


    /**
     * hash to 32 bytes long secret
     * @param key secret
     * @return bytes[32]
     */
    private byte[] hashTo32Bytes(String key) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
        md.update(key.getBytes());
        return md.digest();
    }


    /**
     * encrypt function
     * @param text text to crypted
     * @return encrypted string
     */
    public String encryptString(String text ) throws CryptoException {

        // Create Seed.
        byte[] seed = new byte[IV_SIZE];
        new Random().nextBytes(seed);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(seed);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (InvalidKeyException|InvalidAlgorithmParameterException e) {
            throw new CryptoException("error while init cipher ", e);
        }

        //encrypt message
        byte[] encrypted;
        try {
            encrypted = cipher.doFinal(text.getBytes());
        } catch (IllegalBlockSizeException|BadPaddingException e) {
            throw new CryptoException("error while encrypt message", e);
        }

        return  Base64.getEncoder().encodeToString( ArrayUtils.addAll(cipher.getIV(),encrypted) );

    }

    /**
     * decrypt function
     * @param cryptoText crypted text
     * @return decrypt string
     */
    public String decryptString(String cryptoText ) throws CryptoException {

        if(!isBase64Encoded(cryptoText))
        {
            throw new CryptoException("not a valid encrypted string");
        }

        byte[] encryptedTextBytes =  Base64.getDecoder().decode(cryptoText);

        // Extract IV.
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOfRange(encryptedTextBytes, 0, IV_SIZE));

        // Extract encrypted part.
        byte[] encryptedBytes = Arrays.copyOfRange(encryptedTextBytes, IV_SIZE, encryptedTextBytes.length);


        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (InvalidKeyException|InvalidAlgorithmParameterException e) {
            throw new CryptoException("error while init Cipher", e);
        }

        byte[] decrypted;
        try {
            decrypted = cipher.doFinal(encryptedBytes);
        } catch (IllegalBlockSizeException|BadPaddingException e) {
            throw new CryptoException("error while decrypt text", e);
        }

        return new String(decrypted);

    }

    /**
     * check if string is base64 string
     * @param value string to check
     * @return true if base64
     */
    public static boolean isBase64Encoded(String value)
    {
        //Convert URL-encoding to a reference string
        String checkValue = value.replaceAll("-","+").replaceAll("_","/");
        Matcher matcher = pattern.matcher(checkValue);
        return matcher.find();
    }


}