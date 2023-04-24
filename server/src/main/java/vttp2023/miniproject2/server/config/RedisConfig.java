package vttp2023.miniproject2.server.config;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Bean;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
    // @Value("${spring.redis.host}")
    // private String redisHost;
    
    // @Value("${spring.redis.port}")
    // private Optional<Integer> redisPort;
    
    // @Value("${spring.redis.password}")
    // private String redisPassword;

    private String redisHost = System.getenv("SPRING_REDIS_HOST");
    private Integer redisPort = Integer.valueOf(System.getenv("SPRING_REDIS_PORT")); 
    private String redisPassword = System.getenv("SPRING_REDIS_PASSWORD");
    
    // @Bean("marvelcache") // must match qualifier name in RedisRepo.java
    @Bean
    @Scope("singleton")
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);

        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        // logger.info("redis host port > {redisHost} {redisPort}", redisHost, redisPort);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer());

        RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());
        template.setValueSerializer(
            serializer
        );
        return template;
    }
    


}
