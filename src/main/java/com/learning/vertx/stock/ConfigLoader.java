package com.learning.vertx.stock;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;

public class ConfigLoader {

  public static final String SERVER_PORT = "SERVER_PORT";
  public static final String VERSION = "version";


  public static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = Arrays.asList(SERVER_PORT);

  public static Future<BrokerConfig> load(Vertx vertx) {

    ConfigStoreOptions ymlConfig = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject()
        .put("path", "application.yaml"));

    ConfigStoreOptions envConfig  = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("keys",EXPOSED_ENVIRONMENT_VARIABLES ));

    ConfigStoreOptions sysConfig = new ConfigStoreOptions()
      .setType("sys")
      .setConfig(new JsonObject().put("cache", false ));

    ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
      .addStore(envConfig)
      .addStore(sysConfig)
      .addStore(ymlConfig);

    ConfigRetriever configRetriever =  ConfigRetriever.create(vertx, configRetrieverOptions);

    return configRetriever.getConfig().map(BrokerConfig::from);
  }
}
