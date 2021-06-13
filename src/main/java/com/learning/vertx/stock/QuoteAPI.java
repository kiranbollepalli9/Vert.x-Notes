package com.learning.vertx.stock;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuoteAPI {

  private static final Logger LOG = LoggerFactory.getLogger(QuoteAPI.class);

  public static void attach(Router router) {

    router.get("/quotes/:assetName").handler(routingContext -> {

      String name = routingContext.pathParams().get("assetName");
      LOG.debug(" quote path param {}",name );
      Optional<Quote> maybeQuote = Optional.ofNullable(InMemoryData.getQuoteForAsset(name));
      if (!maybeQuote.isPresent()) {
        JsonObject emptyResponse = new JsonObject();
        emptyResponse.put("message", "Quote for asset " + name + " is not available! ");
        routingContext.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(emptyResponse.toBuffer());
      }else {
        LOG.debug(" Quote api path: {} response {}", routingContext.normalizedPath(), maybeQuote.get().toJsonObject().encodePrettily());
        routingContext.response().end(maybeQuote.get().toJsonObject().toBuffer());
      }

    });
  }
}
