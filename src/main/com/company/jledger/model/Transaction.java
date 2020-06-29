package com.company.jledger.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Builder
@Getter
public class Transaction {

  private final LocalDate date;
  private final String description;
  private final Account fromAccount;
  private final Account toAccount;
  private final Amount fromAmount;
  private Amount toAmount;

  public boolean isValid() {
    if (Objects.equals(fromAccount, toAccount)) {
      log.error("From and to account cannot be the same");
      return false;
    }
    if (fromAmount.getBigDecimal().add(toAmount.getBigDecimal()).intValue() > 0) {
      log.error("From and to amount must be balanced");
      return false;
    }
    return true;
  }

  public String printFor(Label label) {
    if (fromAccount.hasLabel(label)) {
      return String.format("%s\t%s\t%s\t%s",
          date.format(DateTimeFormatter.ISO_DATE),
          description,
          toAccount,
          toAmount);
    }
    if (toAccount.hasLabel(label)) {
      return String.format("%s\t%s\t%s\t%s",
          date.format(DateTimeFormatter.ISO_DATE),
          description,
          fromAccount,
          fromAmount.reverse());
    }
    throw new IllegalArgumentException("This should never happen");
  }
}
