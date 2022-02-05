package org.example.designpattern.pubsub;

public interface ConnectionCallback {
  void onConnected();

  void onDisconnected();
}
