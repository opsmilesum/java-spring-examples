package org.example.designpattern.converter;

import java.time.Instant;
import java.util.Map;

public interface ConverterInterface {
  Instant convert(Instant initial, Map<String, Object> context);
}
