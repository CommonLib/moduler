package com.architecture.extend.baselib.util;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import com.architecture.extend.baselib.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Created by xiongyu on 2016/12/1.
 * 使用ksyStore加密工具类
 */

public class EncryptionUtil {

    private static String alias;

    static {
        alias = BaseApplication.getInstance().getPackageName();
        initKeyStore(alias);
    }

    private static void initKeyStore(String alias) {
        KeyStore keyStore = getAndroidKeyStore();
        if (keyStore == null) {
            return;
        }
        try {
            keyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                createNewKeys(keyStore, alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KeyStore getAndroidKeyStore() {
        try {
            return KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createNewKeys(KeyStore keyStore, String alias) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    spec = new KeyPairGeneratorSpec.Builder(BaseApplication.getInstance())
                            .setAlias(alias)
                            .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                            .setSerialNumber(BigInteger.ONE).setStartDate(start.getTime())
                            .setEndDate(end.getTime()).build();
                }
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    generator.initialize(spec);
                }
                generator.generateKeyPair();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加密方法
     *
     * @param needEncryptWord 　need to encryptWord
     * @return
     */
    public static String encrypt(String needEncryptWord) {
        KeyStore keyStore = getAndroidKeyStore();
        if(keyStore == null){
            return needEncryptWord;
        }
        String encryptStr = "";
        byte[] values = null;
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore
                    .getEntry(alias, null);
            if (needEncryptWord.isEmpty()) {
                return encryptStr;
            }

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inCipher);
            cipherOutputStream.write(needEncryptWord.getBytes("UTF-8"));
            cipherOutputStream.close();

            values = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(values, Base64.DEFAULT);
    }


    public static String decrypt(String needDecryptWord) {
        String decryptStr = "";
        KeyStore keyStore = getAndroidKeyStore();
        if(keyStore == null){
            return needDecryptWord;
        }
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore
                    .getEntry(alias, null);
            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(needDecryptWord, Base64.DEFAULT)),
                    output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            decryptStr = new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }
}
