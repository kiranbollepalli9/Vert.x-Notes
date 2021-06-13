package com.learning.vertx;

import com.learning.vertx.stock.AssetAPI;
import com.learning.vertx.stock.QuoteAPI;
import com.learning.vertx.stock.WatchListAPI;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  private static void errorHandler(RoutingContext errorContext) {
    if (errorContext.failed()) {
      LOG.error(" Error route: {} errorMessage {}",
        errorContext.normalizedPath(),
        errorContext.failure().getMessage());

      errorContext.response()
        .setStatusCode(500)
        .setStatusMessage(" Something went wrong")
        .end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
    }
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router restAPI = Router.router(vertx);
    restAPI.route()
      .handler(BodyHandler.create())
      .failureHandler(MainVerticle::errorHandler);

    AssetAPI.attach(restAPI);
    QuoteAPI.attach(restAPI);
    WatchListAPI.attach(restAPI);

    vertx.createHttpServer()
      .requestHandler(restAPI)
      .exceptionHandler(exception -> LOG.error("Error : {}", exception.getMessage()))
      .listen(Constants.PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.debug("HTTP server started port {}", http.result().actualPort());
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
}
