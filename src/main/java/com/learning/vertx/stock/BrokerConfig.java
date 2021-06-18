package com.learning.vertx.stock;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Objects;

@Value
@Builder
@AllArgsConstructor
public class BrokerConfig {
  int port;
  String version;

  public static BrokerConfig from(JsonObject config) {
    final Integer serverPort = config.getInteger(ConfigLoader.SERVER_PORT);
    if (Objects.isNull(serverPort)) {
      throw new RuntimeException(ConfigLoader.SERVER_PORT + " not configured!");
    }
    final String version = config.getString(ConfigLoader.VERSION);

    if (Objects.isNull(version)) {
      throw new RuntimeException(ConfigLoader.VERSION + " not configured!");
    }
    return BrokerConfig.builder()
      .port(serverPort)
      .version(version)
      .build();
  }
}
