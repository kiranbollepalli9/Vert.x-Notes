package com.learning.vertx;

import com.learning.vertx.stock.Asset;
import com.learning.vertx.stock.InMemoryData;
import com.learning.vertx.stock.WatchList;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

@ExtendWith(VertxExtension.class)
public class TestWatchListAPI extends AbstractRestAPITest {


  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void test_watchlist_api_put_success(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions()
      .setDefaultPort(TEST_SERVER_PORT));

    List<Asset> list = InMemoryData.getAssetsAsList();

    String accountId = UUID.randomUUID().toString();

    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(new WatchList(list).toJsonObject())
      .onComplete(testContext.succeeding(response -> {
        Assertions.assertEquals(200, response.statusCode());
        //Assertions.assertEquals("", response.bodyAsJsonObject().encode());
      })).compose(handler -> {
      client.get("/account/watchlist/" + accountId)
        .send()
        .onComplete(testContext.succeeding(response -> {
          Assertions.assertEquals(200, response.statusCode());
          // Assertions.assertEquals("", response.bodyAsJsonObject().encode());
          testContext.completeNow();
        }));
      return Future.succeededFuture();
    });
  }

  @Test
  void test_watchlist_api_delete_success(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions()
      .setDefaultPort(TEST_SERVER_PORT));

    List<Asset> list = InMemoryData.getAssetsAsList();

    String accountId = UUID.randomUUID().toString();

    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(new WatchList(list).toJsonObject())
      .onComplete(testContext.succeeding(response -> {
        Assertions.assertEquals(200, response.statusCode());
        //Assertions.assertEquals("", response.bodyAsJsonObject().encode());
      })).compose(handler -> {
      client.delete("/account/watchlist/" + accountId)
        .send()
        .onComplete(testContext.succeeding(response -> {
          Assertions.assertEquals(200, response.statusCode());
          Assertions.assertEquals("{\"message\":\"Successfully removed from watchlist!\"}", response.bodyAsJsonObject().encode());
          testContext.completeNow();
        }));
      return Future.succeededFuture();
    });
  }

}
