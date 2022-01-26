package org.example.designpattern.advice;

public interface Advice<T> {
  void onEnter();

  void onSuccess(T t);

  void onError(Exception e);
}
