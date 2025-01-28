package com.sothrose.assetflow_api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AssetflowApiGatewayApplicationTests {

  private static final int REDIS_PORT = 6380;

  @Container
  static GenericContainer<?> redis =
      new GenericContainer<>(DockerImageName.parse("redis:7.0"))
          .withExposedPorts(REDIS_PORT)
          .waitingFor(Wait.forListeningPort())
          .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))
          .withReuse(true);

  @DynamicPropertySource
  static void redisProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", () -> redis.getHost());
    registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
  }

  @Test
  void contextLoads() {}
}
