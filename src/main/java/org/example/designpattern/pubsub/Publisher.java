package org.example.designpattern.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Cleanup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class Publisher {
  private final DataSource dataSource;

  public void publish(String channel, Object object) throws JsonProcessingException, SQLException {
    ObjectMapper objectMapper = new ObjectMapper();

    String payload = objectMapper.writeValueAsString(object);

    @Cleanup Connection connection = dataSource.getConnection();
    PreparedStatement preparedStatement = connection.prepareCall("SELECT pg_notify(?, ?)");
    preparedStatement.setString(1, channel);
    preparedStatement.setString(2, payload);
    preparedStatement.execute();
  }
}
