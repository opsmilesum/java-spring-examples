package org.example.designpattern.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGNotification;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PgConnection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class Subscriber extends Thread implements AutoCloseable {
  private final PGSimpleDataSource pgSimpleDataSource;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, List<Listener<?>>> listenerByChannel;
  List<ConnectionCallback> connectionCallbacks;
  private boolean disabled = false;

  @Override
  public void run() {
    disabled = false;

    while (!disabled) {
      try {
        subscribe();
        connectionCallbacks.forEach(ConnectionCallback::onConnected);
      } catch (Exception e) {
        connectionCallbacks.forEach(ConnectionCallback::onDisconnected);
        Random random = new Random();
        try {
          Thread.sleep(random.nextLong());
        } catch (InterruptedException sleepException) {
          log.error("Sleep failed#{}", sleepException.getMessage());
        }
      }
    }
  }

  public final void disable() {
    this.disabled = true;
  }

  void subscribe() throws SQLException {
    @Cleanup PgConnection pgConnection = (PgConnection) pgSimpleDataSource.getConnection();

    for (String channel : listenerByChannel.keySet()) {
      Statement statement = pgConnection.createStatement();
      statement.execute("LISTEN \"" + channel + "\"");
    }

    subscribe(pgConnection);
  }

  void subscribe(PgConnection pgConnection) throws SQLException {
    PGNotification[] pgNotifications = pgConnection.getNotifications(1000);

    if (pgNotifications == null) {
      return;
    }

    handleNotification(pgNotifications);
  }

  void handleNotification(PGNotification[] pgNotifications) {
    for (PGNotification pgNotification : pgNotifications) {
      String channel = pgNotification.getName();
      String payload = pgNotification.getParameter();

      List<Listener<?>> listeners = listenerByChannel.get(channel);
      listeners.forEach(listener -> callListener(listener, payload));
    }
  }

  void callListener(Listener<?> listener, String payload) {
    Class<?> dataClass = listener.dataClass;
    Object object = null;

    try {
      object = objectMapper.readValue(payload, dataClass);
    } catch (Exception e) {
      // log.error("Parse payload failed");
    }

    ((Consumer<Object>) listener.getConsumer()).accept(object);
  }

  @Override
  public void close() throws Exception {}

  @Getter
  public static class Listener<T> {
    private Class<T> dataClass;
    private Consumer<T> consumer;
  }
}
