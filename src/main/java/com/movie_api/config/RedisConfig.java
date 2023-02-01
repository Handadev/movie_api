package com.movie_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.movie_api.db.collection.Test;
import com.movie_api.db.collection.TestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.ArrayList;

@EnableCaching
@Configuration
public class RedisConfig {

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(10))
                .build();

        RedisSentinelConfiguration config = new RedisSentinelConfiguration()
                .master("mymaster")
//                .sentinel("localhost", 26000)
//                .sentinel("localhost", 26001)
//                .sentinel("localhost", 26002)
                .sentinel("192.168.0.116", 26000)
                .sentinel("192.168.0.179", 26000)
                .sentinel("192.168.0.108", 26000)
        ;
        config.setPassword("1234qwer");
        return new LettuceConnectionFactory(config, lettuceClientConfiguration);
//        return new LettuceConnectionFactory(config);
    }
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//
//
//        RedisSentinelConfiguration config = new RedisSentinelConfiguration()
//                .master("mymaster")
////                .sentinel("localhost", 26000)
////                .sentinel("localhost", 26001)
////                .sentinel("localhost", 26002)
//                .sentinel("192.168.0.116", 26000)
//                .sentinel("192.168.0.179", 26000)
//                .sentinel("192.168.0.108", 26000)
//                ;
//        config.setPassword("1234qwer");
//        JedisConnectionFactory factory = new JedisConnectionFactory(config);
//        return factory;
//    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    RedisCacheConfiguration defaultConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
    }

    @Bean
    public CacheManager userCacheManager(RedisConnectionFactory factory, RedisCacheConfiguration defaultConfiguration) {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TestDTO.class);
        System.out.println("collectionType@@@@@@@@@@@@@@@@@@@@ = " + collectionType);
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(factory)
                .cacheDefaults(defaultConfiguration
                        .entryTtl(Duration.ofSeconds(30))
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(collectionType))))
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer())))
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(TestDTO.class))))

                .build();
    }
}
