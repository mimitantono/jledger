package com.company.jledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.company.jledger.model.Currency;
import com.company.jledger.model.FailConversion;
import com.company.jledger.model.Transaction;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransactionImporterTest {

  private final Currency defaultCurrency = new Currency("USD");
  private final SimpleDateFormat importFormat = new SimpleDateFormat("dd.MM.yyyy");

  @Test
  void parsesTransactionWithoutExplicitToAmount() {
    TransactionImporter importer = new TransactionImporter(defaultCurrency, importFormat);

    List<FailConversion> failures = importer.readTransactions(Collections.singletonList(
        "01.02.2020,Salary,Income:Salary,1000,Assets:Bank"));

    assertTrue(failures.isEmpty(), () -> "Unexpected failures: " + failures);
    assertEquals(1, importer.getTransactions().size());
    Transaction transaction = importer.getTransactions().get(0);
    assertEquals(LocalDate.of(2020, 2, 1), transaction.getDate());
    assertEquals(new BigDecimal("-1000"), transaction.getToAmount().getBigDecimal());
    assertEquals(defaultCurrency, transaction.getToAmount().getCurrency());
  }

  @Test
  void parsesTransactionWithExplicitBalancedToAmount() {
    TransactionImporter importer = new TransactionImporter(defaultCurrency, importFormat);

    List<FailConversion> failures = importer.readTransactions(Collections.singletonList(
        "05.03.2021,Coffee,Assets:Bank,-5.25,Expenses:Food,5.25"));

    assertTrue(failures.isEmpty(), () -> "Unexpected failures: " + failures);
    assertEquals(1, importer.getTransactions().size());
    Transaction transaction = importer.getTransactions().get(0);
    assertEquals(new BigDecimal("5.25"), transaction.getToAmount().getBigDecimal());
    assertEquals(new BigDecimal("-5.25"), transaction.getFromAmount().getBigDecimal());
  }

  @Test
  void recordsFailureWhenExplicitToAmountIsNotBalanced() {
    TransactionImporter importer = new TransactionImporter(defaultCurrency, importFormat);

    List<FailConversion> failures = importer.readTransactions(Collections.singletonList(
        "10.04.2021,Adjustment,Assets:Bank,100,Expenses:Fees,-99.50"));

    assertEquals(1, failures.size());
    assertTrue(importer.getTransactions().isEmpty());
  }
}
