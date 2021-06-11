package com.learning.vertx.dto;

public class Ping {

  private String message;
  private int id;

  public String getMessage() {
    return message;
  }

  public Ping(String message, int id) {
    this.message = message;
    this.id = id;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Ping{" +
      "message='" + message + '\'' +
      ", id=" + id +
      '}';
  }
}
