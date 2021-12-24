package com.ozgeek.utils;

public class Util {

  public static void sleep(long i) {
    try {
      Thread.sleep(i);
    } catch (InterruptedException ie) {}
  }
}
