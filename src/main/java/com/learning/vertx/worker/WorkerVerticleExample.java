package com.learning.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticleExample extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WorkerVerticleExample.class.getName());

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerVerticleExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug(" Name: {} Thread {}", WorkerVerticleExample.class.getName(), Thread.currentThread().getName());
    startPromise.complete();
    vertx.executeBlocking(event -> {
        LOG.debug(" Executing blocking code {} ", Thread.currentThread().getName());
        //event.complete();

        try {
          Thread.sleep(5000);
          event.complete();
        } catch (InterruptedException e) {
          e.printStackTrace();
          event.fail(e);
        }
      },
      result -> {
          if (result.succeeded()) {
            LOG.debug(" Executing blocking code  completed {}", Thread.currentThread().getName());
          }else {
            LOG.debug(" Error in Executing  blocking code {} ", result.cause().getMessage());
          }
      });
  }
}
