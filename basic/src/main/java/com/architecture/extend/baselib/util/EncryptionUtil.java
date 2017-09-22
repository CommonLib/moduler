package com.architecture.extend.baselib.util;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import com.architecture.extend.baselib.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
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
                bytes[i] = values.get(i);
            }

            decryptStr = new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }


    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception
     */
    public static String desEncode(String key,String data) {
        if(data == null)
            return null;
        try{
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        }catch(Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String desDecode(String key,String data) {
        if(data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 二行制转字符串
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }


    private static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length/2];
        for (int n = 0; n < b.length; n+=2) {
            String item = new String(b,n,2);
            b2[n/2] = (byte)Integer.parseInt(item,16);
        }
        return b2;
    }

    /**
     * 获取字符串的 MD5
     */
    public static String md5Encode(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", b));
            }
            return hexString.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取文件的 MD5
     */
    public static String md5Encode(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream inputStream = new FileInputStream(file);
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
            //必须把文件读取完毕才能拿到md5
            byte[] buffer = new byte[4096];
            while (digestInputStream.read(buffer) > -1) {
            }
            MessageDigest digest = digestInputStream.getMessageDigest();
            digestInputStream.close();
            byte[] md5 = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
