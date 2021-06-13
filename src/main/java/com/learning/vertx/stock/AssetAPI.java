package com.learning.vertx.stock;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetAPI {

  private static final Logger LOG = LoggerFactory.getLogger(AssetAPI.class);

  public static void attach(Router router) {
    router.get("/assets").handler(routingContext -> {
      JsonArray response = InMemoryData.getAssetList();

      LOG.debug(" Asset api path: {} response {}", routingContext.normalizedPath(), response.encodePrettily());
      routingContext.response().end(response.toBuffer());

    });
  }
}
