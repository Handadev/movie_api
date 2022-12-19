package com.movie_api;

import com.movie_api.util.Crypto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

@SpringBootTest
class MovieApiApplicationTests {

    @Autowired Crypto crypto;
    @Test
    void encrypt() {
        String text = "냠냠";


        String enc = crypto.encodeAES256(text);

        System.out.println("enc = " + enc);

        String dec = crypto.decodeAES256(enc);

        System.out.println("dec = " + dec);

    }

}
