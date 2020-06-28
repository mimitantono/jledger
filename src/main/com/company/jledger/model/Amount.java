package com.company.jledger.model;


import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Amount {

  private final Currency currency;
  private final BigDecimal bigDecimal;
}
