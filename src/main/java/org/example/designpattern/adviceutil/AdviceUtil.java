package org.example.designpattern.adviceutil;

import java.util.function.Supplier;

public class AdviceUtil<T> {
  public static <T> T advice(Supplier<T> action, Advice<T> advice, boolean activated) {
    if (!activated) {
      return action.get();
    }

    advice.onEnter();
    try {
      T t = action.get();
      advice.onSuccess(t);
      return t;
    } catch (Exception e) {
      advice.onError(e);
      throw e;
    }
  }
}
