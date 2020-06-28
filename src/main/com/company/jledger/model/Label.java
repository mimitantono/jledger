package com.company.jledger.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString(exclude = "level")
public class Label {

  private final int level;
  private final String namespace;
}
