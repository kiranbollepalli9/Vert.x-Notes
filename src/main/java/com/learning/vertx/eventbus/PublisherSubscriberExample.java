package com.learning.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PublisherSubscriberExample {

  private static final Logger LOG = LoggerFactory.getLogger( PublisherSubscriberExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new Subscriber1Verticle());
    vertx.deployVerticle(new Subscriber2Verticle());

  }

  static class PublisherVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger( PublisherVerticle.class );

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), handler -> {
        vertx.eventBus().publish(PublisherVerticle.class.getName(), "Hai there");
      });

    }
  }

  static class Subscriber1Verticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger( Subscriber1Verticle.class );

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.eventBus().<String>consumer(PublisherVerticle.class.getName(),
        message -> LOG.debug(" Received: " + message.body()));

    }
  }

  static class Subscriber2Verticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger( Subscriber2Verticle.class );

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.eventBus().<String>consumer(PublisherVerticle.class.getName(),
        message -> LOG.debug(" Received: " + message.body()));

    }
  }

}
