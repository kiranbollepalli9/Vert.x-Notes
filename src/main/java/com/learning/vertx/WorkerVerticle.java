package com.learning.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {
 private static final Logger LOG =  LoggerFactory.getLogger(WorkerVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug(" Name: {} Thread {}" , WorkerVerticle.class.getName(), Thread.currentThread().getName()) ;
    LOG.debug(" Config : " + context.config());
    startPromise.complete();
  }
}
