package com.learning.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {

  private static final Logger LOG = LoggerFactory.getLogger( PointToPointExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SenderVerticle());
    vertx.deployVerticle(new ReceiverVerticle());

  }

  static class SenderVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger( SenderVerticle.class );

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.setPeriodic(1000 , handler -> {
        vertx.eventBus().send(SenderVerticle.class.getName(), "Hai there");
      });

    }
  }

  static class ReceiverVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger( ReceiverVerticle.class );

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.eventBus().<String>consumer(SenderVerticle.class.getName(),
        message -> LOG.debug(" Received: " + message.body()));

    }
  }

}
