package org.example.designpattern.Observer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class ObserverTest {

  Observer<String, updateCallback> observer = new Observer<>();

  @BeforeEach
  void setUp() {
    observer.register(
        "K1",
        new updateCallback() {
          @Override
          public void beforeUpdate() {
            log.info("K1-beforeUpdate");
          }
        });
    observer.register(
        "K1",
        new updateCallback() {
          @Override
          public void afterUpdate() {
            log.info("K1-afterUpdate");
          }
        });
  }

  @Test
  void callbackObserver() {
    observer.callbackObserver("K1", updateCallback::beforeUpdate);
    observer.callbackObserver("K1", updateCallback::afterUpdate);
  }

  /* output:
    K1-beforeUpdate
    default-beforeUpdate
    default-afterUpdate
    K1-afterUpdate
  * */

  public interface updateCallback {
    default void beforeUpdate() {
      log.info("default-beforeUpdate");
    }

    default void afterUpdate() {
      log.info("default-afterUpdate");
    }
  }
}
