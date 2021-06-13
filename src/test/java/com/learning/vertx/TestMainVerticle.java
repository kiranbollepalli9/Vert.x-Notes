package com.learning.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void test_asset_api_success(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(Constants.PORT));
    client.get("/assets").send().onComplete(testContext.succeeding(response -> {
      Assertions.assertEquals(200, response.statusCode());
      Assertions.assertNotEquals(0, response.bodyAsJsonArray().size());
      testContext.completeNow();
    }));
  }

  @Test
  void test_quote_api_success(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(Constants.PORT));
    client.get("/quotes/TATA").send().onComplete(testContext.succeeding(response -> {
      Assertions.assertEquals(200, response.statusCode());
      Assertions.assertFalse(response.bodyAsJsonObject().isEmpty());
      testContext.completeNow();
    }));
  }
}
