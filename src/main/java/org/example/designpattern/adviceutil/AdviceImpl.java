package org.example.designpattern.adviceutil;

public class AdviceImpl {
  static class Student {}

  public static Student run() {
    return AdviceUtil.advice(
        () -> new Student(),
        new Advice<>() {
          @Override
          public void onEnter() {
            System.out.println("Enter...");
          }

          @Override
          public void onSuccess(Student student) {
            System.out.println("Handle succeed!");
          }

          @Override
          public void onError(Exception e) {
            System.out.println("Handle failed...");
          }
        },
        true);
  }
}
