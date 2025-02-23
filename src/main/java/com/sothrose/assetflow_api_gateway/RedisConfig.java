package com.sothrose.assetflow_api_gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.redis.host:assetflow-api-gateway-redis}")
  private String redisHost;

  @Value("${spring.redis.port:6379}")
  private int redisPort;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    var config = new RedisStandaloneConfiguration();
    config.setHostName(redisHost);
    config.setPort(redisPort);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "apiGatewayRedisTemplate")
  public RedisTemplate<String, String> apiGatewayRedisTemplate(LettuceConnectionFactory factory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(RedisSerializer.string());
    template.setValueSerializer(RedisSerializer.string());
    template.setHashKeySerializer(RedisSerializer.string());
    template.setHashValueSerializer(RedisSerializer.string());
    template.afterPropertiesSet();
    return template;
  }

  @Bean
  public CircuitBreakerRegistry circuitBreakerRegistry() {
    return CircuitBreakerRegistry.ofDefaults();
  }

  @Bean
  public RetryRegistry retryRegistry() {
    return RetryRegistry.ofDefaults();
  }
}
