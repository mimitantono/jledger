package com.company.jledger.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Account {

  private static final String DELIMITER = ":";
  private final String namespace;

  public List<Label> getLabels() {
    List<Label> labels = new ArrayList<>();
    String[] split = namespace.split(DELIMITER);
    for (int i = 0; i < split.length; i++) {
      labels.add(new Label(Arrays.copyOfRange(split, 0, i + 1)));
    }
    return labels;
  }

  public boolean hasLabel(Label label) {
    return getLabels().contains(label);
  }

  @Override
  public String toString() {
    return namespace;
  }
}
