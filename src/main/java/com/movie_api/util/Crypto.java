package com.movie_api.util;

import com.movie_api.properties.CryptoProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;


@Component
public class Crypto {
    @Autowired CryptoProperty cryptoProperty;

    public String encodeAES256(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(cryptoProperty.getAes256key().getBytes(), "AES"), new IvParameterSpec(cryptoProperty.getIv().getBytes()));
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decodeAES256(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(cryptoProperty.getAes256key().getBytes(), "AES"), new IvParameterSpec(cryptoProperty.getIv().getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(text)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encodeSHA256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : md.digest()) {
                stringBuffer.append(String.format("%02x", b));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
