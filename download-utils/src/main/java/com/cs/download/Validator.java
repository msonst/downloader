package com.cs.download;

public interface Validator<T> {
  public boolean isValid(T value); 
}
