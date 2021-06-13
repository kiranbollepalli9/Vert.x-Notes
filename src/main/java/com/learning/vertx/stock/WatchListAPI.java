package com.learning.vertx.stock;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class WatchListAPI {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListAPI.class);

  public static final String PATH = "/account/watchlist/:accountId";

  public static void attach(Router router) {

    router.get(PATH).handler(routingContext -> {

      String accountId = routingContext.pathParams().get("accountId");
      LOG.debug(" watchlist path param {}", accountId);
      Optional<WatchList> maybeWatchList = Optional.ofNullable(InMemoryData.getWatchList(accountId));
      if (!maybeWatchList.isPresent()) {
        JsonObject emptyResponse = new JsonObject();
        emptyResponse.put("message", "WatchList for asset " + maybeWatchList + " is not available! ");
        routingContext.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(emptyResponse.toBuffer());
      } else {
        LOG.debug(" WatchList api path: {} response {}", routingContext.normalizedPath(), maybeWatchList.get().toJsonObject().encodePrettily());
        routingContext.response().end(maybeWatchList.get().toJsonObject().toBuffer());
      }

    });

    router.put(PATH).handler(routingContext -> {

      String accountId = routingContext.pathParams().get("accountId");
      LOG.debug(" watchlist path param {}", accountId);
      JsonObject bodyJson = routingContext.getBodyAsJson();
      WatchList watchList = bodyJson.mapTo(WatchList.class);
      boolean result = InMemoryData.putWatchList(accountId, watchList);
      if (!result) {
        JsonObject emptyResponse = new JsonObject();
        emptyResponse.put("message", "Failed to add watchlist ");
        routingContext.response()
          .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
          .end(emptyResponse.toBuffer());
      } else {
        //LOG.debug(" WatchList api path: {} response {}", routingContext.normalizedPath(), maybeWatchList.get().toJsonObject().encodePrettily());
        routingContext.response().end(new JsonObject().put("message", "Watchlist added succesfully!").toBuffer());
      }

    });

    router.delete(PATH).handler(routingContext -> {

      String accountId = routingContext.pathParams().get("accountId");
      LOG.debug(" watchlist path param {}", accountId);
      boolean result = InMemoryData.deleteWatchList(accountId);
      if (!result) {
        JsonObject emptyResponse = new JsonObject();
        emptyResponse.put("message", "Failed to remove from watchlist ");
        routingContext.response()
          .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
          .end(emptyResponse.toBuffer());
      } else {
        //LOG.debug(" WatchList api path: {} response {}", routingContext.normalizedPath(), maybeWatchList.get().toJsonObject().encodePrettily());
        routingContext.response().end(new JsonObject().put("message", "Successfully removed from watchlist!").toBuffer());
      }

    });
  }
}
