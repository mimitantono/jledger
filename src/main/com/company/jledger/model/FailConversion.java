package com.company.jledger.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class FailConversion {

  private final int lineNumber;
  private final String s;
  private final Exception e;
}
