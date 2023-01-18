package com.movie_api.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Configuration
public class MongoConfig {

    private final String keyStoreFile = "src/main/resources/keystore/keystore2.jks";
    private final String keyStorePassword = "1234qwer";
    private final String keyAlias = "mongo-client-test";
    private final String trustStoreFile = "src/main/resources/keystore/truststore2.jks";
    private final String trustStorePassword = "1234qwer";

    @Bean
    public MongoClientSettings mongoClientSettings(SSLContext mongoSSLContext, X509Certificate mongoClientCertificate) {
        return MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress("localhost", 9911))))
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.context(mongoSSLContext);
                    builder.invalidHostNameAllowed(true);
                })
                .credential(MongoCredential.createMongoX509Credential(mongoClientCertificate.getSubjectX500Principal().getName()))
                .build();
    }

    @Bean
    public SSLContext mongoSSLContext() throws GeneralSecurityException, IOException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new FileInputStream(keyStoreFile)) {
            keystore.load(in, keyStorePassword.toCharArray());
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new FileInputStream(trustStoreFile)) {
            trustStore.load(in, trustStorePassword.toCharArray());
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }

    @Bean
    public X509Certificate mongoClientCertificate() throws GeneralSecurityException, IOException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new FileInputStream(keyStoreFile)) {
            keystore.load(in, keyStorePassword.toCharArray());
        }
        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, keyStorePassword.toCharArray());
        X509KeyManager keyManager = (X509KeyManager) keyManagerFactory.getKeyManagers()[0];
        return keyManager.getCertificateChain(keyAlias)[0];
    }

}
