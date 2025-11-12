package com.company.jledger.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TransactionTest {

  private static final Currency DEFAULT_CURRENCY = new Currency("USD");

  @Test
  void transactionWithIdenticalAccountsIsInvalid() {
    Account account = new Account("Assets:Cash");
    Transaction transaction = Transaction.builder()
        .date(LocalDate.now())
        .description("Transfer")
        .fromAccount(account)
        .toAccount(account)
        .fromAmount(amount("10"))
        .toAmount(amount("-10"))
        .build();

    assertFalse(transaction.isValid());
  }

  @Test
  void transactionWithBalancedAmountsIsValid() {
    Transaction transaction = Transaction.builder()
        .date(LocalDate.now())
        .description("Deposit")
        .fromAccount(new Account("Income:Salary"))
        .toAccount(new Account("Assets:Bank"))
        .fromAmount(amount("100"))
        .toAmount(amount("-100"))
        .build();

    assertTrue(transaction.isValid());
  }

  @Test
  void transactionWithFractionalImbalanceIsInvalid() {
    Transaction transaction = Transaction.builder()
        .date(LocalDate.now())
        .description("Adjustment")
        .fromAccount(new Account("Assets:Cash"))
        .toAccount(new Account("Expenses:Misc"))
        .fromAmount(amount("100"))
        .toAmount(amount("-100.50"))
        .build();

    assertFalse(transaction.isValid());
  }

  @Test
  void getRowThrowsWhenLabelDoesNotMatchEitherAccount() {
    Transaction transaction = Transaction.builder()
        .date(LocalDate.of(2020, 1, 1))
        .description("Deposit")
        .fromAccount(new Account("Income:Salary"))
        .toAccount(new Account("Assets:Bank"))
        .fromAmount(amount("100"))
        .toAmount(amount("-100"))
        .build();

    assertThrows(IllegalArgumentException.class,
        () -> transaction.getRow(new Label("Expenses")));
  }

  private Amount amount(String value) {
    return Amount.builder()
        .currency(DEFAULT_CURRENCY)
        .bigDecimal(new BigDecimal(value))
        .build();
  }
}
