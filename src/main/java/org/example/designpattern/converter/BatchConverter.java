package org.example.designpattern.converter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BatchConverter implements ConverterInterface {
  private final List<ConverterInterface> converterList;

  public BatchConverter(List<ConverterInterface> converterList) {
    this.converterList = converterList;
  }

  @Override
  public Instant convert(Instant instant, Map<String, Object> context) {
    for (ConverterInterface converter : converterList) {
      instant = converter.convert(instant, context);
    }
    return instant;
  }
}
