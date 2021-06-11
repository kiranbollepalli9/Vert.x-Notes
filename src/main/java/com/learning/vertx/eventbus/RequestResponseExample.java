package com.learning.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  public static final String  ADDRESS = "com.learning.vertx.eventbus.PingVerticle";
  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExample.class.getName());

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    LOG.debug("Deploying  verticles");
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {
    private final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class.getName());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //LOG.debug(" deployed verticle {} ", PingVerticle.class.getName());
      EventBus eventBus = vertx.eventBus();
      eventBus.<String>request(ADDRESS, "Hai How are you!", replyHandler -> {
         if(replyHandler.succeeded()) {
           LOG.debug(" Reply: {} ", replyHandler.result().body());
         }
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    private final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class.getName());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //LOG.debug(" deployed verticle {} ", PongVerticle.class.getName());
      EventBus eventBus = vertx.eventBus();
      eventBus.<String>consumer(ADDRESS, handler -> {
        LOG.debug(" Request: {} ", handler.body());
        handler.reply(" I am fine thanks");
      });
    }
  }
}
