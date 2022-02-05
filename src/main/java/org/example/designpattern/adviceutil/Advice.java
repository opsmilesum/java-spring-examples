package org.example.designpattern.adviceutil;

public interface Advice<T> {
  void onEnter();

  void onSuccess(T t);

  void onError(Exception e);
}
