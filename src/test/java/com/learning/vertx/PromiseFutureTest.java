package com.learning.vertx;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class PromiseFutureTest {
  private static final Logger LOG = LoggerFactory.getLogger( PromiseFutureTest.class );


  @Test
  void promise_success(Vertx vertx, VertxTestContext testContext) {
    Promise<String> promise = Promise.promise();
     LOG.debug("Start of test");
    vertx.setTimer(500, handler -> {
      promise.complete("Success");
      LOG.debug("Success");
      testContext.completeNow();
    });
    LOG.debug("End of test");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext testContext) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start of test");
    vertx.setTimer(500, handler -> {
      promise.fail(new RuntimeException("Promise exception!"));
      LOG.debug("Fail");
      testContext.completeNow();
    });
    LOG.debug("End of test");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext testContext) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start of test");
    vertx.setTimer(500, handler -> {
      promise.complete("Success");
     // promise.fail(new RuntimeException("Promise exception!"));

      LOG.debug("Timer done!");
    });
    Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOG.debug("Future success {}", result);
      testContext.completeNow();

    }).onFailure(error -> {
      LOG.debug("Future failure {}", error.getMessage());
      testContext.completeNow();
    });
    LOG.debug("End of test");
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext testContext) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start of test");
    vertx.setTimer(500, handler -> {
      promise.fail(new RuntimeException("Promise exception!"));

      LOG.debug("Timer done!");
    });
    Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOG.debug("Future success {}", result);
      testContext.completeNow();

    }).onFailure(error -> {
      LOG.debug("Future failure {}", error.getMessage());
      testContext.completeNow();
    });
    LOG.debug("End of test");
  }

}
