package com.learning.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class StartVerticle extends AbstractVerticle {
  private static final Logger LOG =  LoggerFactory.getLogger(StartVerticle.class.getName());

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new StartVerticle());
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    LOG.debug(" Name: {} Thread {}" , StartVerticle.class.getName(), Thread.currentThread().getName()) ;

    DeploymentOptions options = new DeploymentOptions()
       .setInstances(2)
       .setConfig(new JsonObject()
         .put("id", UUID.randomUUID().toString())
         .put("name", WorkerVerticle.class.getName())
       );
     vertx.deployVerticle(WorkerVerticle.class.getName(), options, handler -> {
       LOG.debug(" StartVerticle deployed: {} " , handler.result());
     });
    startPromise.complete();
  }
}
