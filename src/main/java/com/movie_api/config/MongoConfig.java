package com.movie_api.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

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
import java.util.Collections;

@Configuration
public class MongoConfig {

    private final String keyStoreFile = "src/main/resources/keystore/keystore2.jks";
    private final String keyStorePassword = "1234qwer";
    private final String keyAlias = "mongo-client-test";
    private final String trustStoreFile = "src/main/resources/keystore/truststore2.jks";
    private final String trustStorePassword = "1234qwer";

    // X509 인증설정이된 MongoDB 연결 init 설정
    @Bean
    public MongoClient initClient(SSLContext mongoSSLContext, X509Certificate mongoClientCertificate) {
        ServerAddress mongos1 = new ServerAddress("localhost", 9911);
        ServerAddress mongos2 = new ServerAddress("localhost", 9912);
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder -> {
                            builder.hosts(Arrays.asList(mongos1, mongos2));
                            builder.mode(ClusterConnectionMode.MULTIPLE);    // 해당 설정들은 공식문서에서 어떻게 사용하는지 없음
                            builder.requiredClusterType(ClusterType.SHARDED);//
                        })
                        .applyToSslSettings(builder -> {
                            builder.enabled(true);
                            builder.context(mongoSSLContext);
                            builder.invalidHostNameAllowed(true); // MongoDB 인스턴스와 같은 호스트에 있다면 false 혹은 이 명령어를 지움
                        })
                        .credential(MongoCredential.createMongoX509Credential(mongoClientCertificate.getSubjectX500Principal().getName()))
                        .build()
        );
    }

    // 연결할 MongoDB database 설정
    @Bean
    public MongoTemplate mongoTemplate(MongoClient client) {
        return new MongoTemplate(client, "test");
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
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, keyStorePassword.toCharArray());
        X509KeyManager keyManager = (X509KeyManager) keyManagerFactory.getKeyManagers()[0];
        return keyManager.getCertificateChain(keyAlias)[0];
    }

}
