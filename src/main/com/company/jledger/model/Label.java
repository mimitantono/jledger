package com.company.jledger.model;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Label {

  private final String namespace;

  public Label(String[] labels) {
    namespace = Arrays.stream(labels).collect(Collectors.joining(":"));
  }

  @Override
  public String toString() {
    return namespace;
  }
}
