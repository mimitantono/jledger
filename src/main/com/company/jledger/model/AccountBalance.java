package com.company.jledger.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class AccountBalance {

  private final Label label;
  private final BigDecimal bigDecimal;

  public String getLabelNamespace() {
    return label.getNamespace();
  }

  @Override
  public String toString() {
    return label.getNamespace() + "\t\t" + bigDecimal;
  }
}
