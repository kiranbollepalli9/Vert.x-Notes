package com.learning.vertx.eventbus;

import com.learning.vertx.dto.Ping;
import com.learning.vertx.dto.Pong;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMessageExample {
  public static final String  ADDRESS = CustomMessageExample.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(CustomMessageExample.class.getName());

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    LOG.debug("Deploying  verticles");
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
      return ar -> {
        if (ar.failed()) {
          LOG.error("Deployment Error {}", ar.cause().getMessage());
        }
      };
  }

  static class PingVerticle extends AbstractVerticle {

    private final Logger LOG = LoggerFactory.getLogger(PingVerticle.class.getName());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //LOG.debug(" deployed verticle {} ", PingVerticle.class.getName());
      EventBus eventBus = vertx.eventBus();
      Ping ping = new Ping("new message ", 10);
      eventBus.<Pong>request(ADDRESS, ping, replyHandler -> {
         if(replyHandler.succeeded()) {
           LOG.debug(" Reply: {} ", replyHandler.result().body().toString());
         }
      });
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private final Logger LOG = LoggerFactory.getLogger(PongVerticle.class.getName());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //LOG.debug(" deployed verticle {} ", PongVerticle.class.getName());
      EventBus eventBus = vertx.eventBus();
      eventBus.<Ping>consumer(ADDRESS, handler -> {
        LOG.debug(" Request: {} ", handler.body());
        handler.reply(new Pong(2000));
      }).exceptionHandler(errorHandler -> {
        LOG.error(" Error on event bus : {} ", errorHandler.getMessage());

      });
    }
  }
}
