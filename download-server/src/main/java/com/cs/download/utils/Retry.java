package com.cs.download.utils;

import java.util.concurrent.Callable;

public class Retry {

  public static <T> T times(int times, Callable<T> method, Validator<T> validator) throws RetryException  {
    T ret = null;

    for (int i = 0; (i < times && !validator.isValid(ret)); i++) {
      try {
        ret = method.call();
      } catch (Exception e) {
      }
    }

    if(!validator.isValid(ret))throw new RetryException("Max retries reached");
    
    return ret;
  }
}
