package jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ObjectMapperTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test_read_value() throws JsonProcessingException {
        String json = "{ \"color\" : \"CV-Unmapped\", \"type\" : \"BMW\"}";
        Car car = objectMapper.readValue(json, Car.class);
        Assertions.assertEquals(car.getColor(), "CV-Unmapped");
    }
}
