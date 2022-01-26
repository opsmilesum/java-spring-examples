package org.example.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regex {

  @Test
  void matcherGroup() {
    String source = "Booking Number: 1000, balabala";

    String patternString = "Booking Number: (?<bookingNumber>[A-Z0-9]+).*";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(source);

    Assertions.assertTrue(matcher.find());
    Assertions.assertEquals(matcher.groupCount(), 1);
    Assertions.assertEquals(matcher.group("bookingNumber"), "1000");
  }
}
