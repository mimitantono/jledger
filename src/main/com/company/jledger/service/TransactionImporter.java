package com.company.jledger.service;

import com.company.jledger.model.Account;
import com.company.jledger.model.Amount;
import com.company.jledger.model.Currency;
import com.company.jledger.model.FailConversion;
import com.company.jledger.model.Transaction;
import com.google.common.base.Splitter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

@ToString
@Log4j
@RequiredArgsConstructor
public class TransactionImporter {

  private static final DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static Splitter SPLITTER = Splitter
      .on(Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

  private final Currency defaultCurrency;
  private final DateFormat dateFormat;

  @Getter
  private final List<Transaction> transactions = new ArrayList<>();

  public List<FailConversion> readTransactions(List<String> lines) {
    List<FailConversion> failConversions = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      try {
        transactions.add(convert(lines.get(i)));
      } catch (Exception e) {
        failConversions.add(new FailConversion(i, lines.get(i), e));
      }
    }
    return failConversions;
  }


  private Transaction convert(String s) throws ParseException {
    List<String> columns = SPLITTER.splitToList(s);
    LocalDate localDate = extractDate(columns.get(0));
    Account fromAccount = extractAccount(columns.get(2));
    Amount fromAmount = extractAmount(columns.get(3));
    Account toAccount = extractAccount(columns.get(4));
    Amount toAmount = reverse(fromAmount);
    if (columns.size() > 5) {
      toAmount = extractAmount(columns.get(5));
    }
    Transaction transaction = Transaction.builder()
        .date(localDate)
        .description(columns.get(1))
        .fromAccount(fromAccount)
        .toAccount(toAccount)
        .fromAmount(fromAmount)
        .toAmount(toAmount)
        .build();
    if (!transaction.isValid()) {
      throw new IllegalArgumentException("Invalid transaction");
    }
    return transaction;
  }

  private Amount reverse(Amount fromAmount) {
    return Amount.builder()
        .bigDecimal(fromAmount.getBigDecimal().negate())
        .currency(fromAmount.getCurrency())
        .build();
  }

  private Amount extractAmount(String column) {
    String[] split = column.split("\\s");
    return Amount.builder()
        .currency(split.length == 1 ? defaultCurrency : new Currency(split[1]))
        .bigDecimal(new BigDecimal(split[0]))
        .build();
  }

  private Account extractAccount(String column) {
    return new Account(column);
  }

  private LocalDate extractDate(String column) throws ParseException {
    return LocalDate.parse(localDateFormat.format(dateFormat.parse(column)));
  }
}
