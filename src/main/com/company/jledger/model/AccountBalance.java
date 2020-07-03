package com.company.jledger.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountBalance {

  private final Label label;
  private final BigDecimal bigDecimal;

  public static String[] getColumnNames() {
    return new String[]{"Account name", "Balance"};
  }

  public String getLabelNamespace() {
    return label.getNamespace();
  }
}
