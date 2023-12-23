package com.cs.download.utils;

public class RetryException extends Exception {
  private static final long serialVersionUID = 4982147819611946132L;

  public RetryException(String string) {
    super(string);
  }

}
