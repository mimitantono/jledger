package com.company.jledger.service;

import com.company.jledger.model.Account;
import com.company.jledger.model.AccountBalance;
import com.company.jledger.model.Label;
import com.company.jledger.model.Transaction;
import dnl.utils.text.table.TextTable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.log4j.Log4j;

@Log4j
public class ReportGenerator {

  private final List<Transaction> transactions;
  private final Map<Label, List<Transaction>> transactionMap;

  public ReportGenerator(List<Transaction> transactions) {
    this.transactions = transactions;
    this.transactionMap = organizeByLabels();
  }

  public void generateReport() {
    List<AccountBalance> accountBalances = calculateBalance(transactionMap);
    new ReportDecorator().printBalances(accountBalances);
  }

  public void listAccountTransactions(Label label) {
    System.out.println("Transactions for: " + label);
    if (!transactionMap.containsKey(label)) {
      System.out.println("N/A");
      return;
    }
    new ReportDecorator().printTransactions(transactionMap, label);
  }

  private List<AccountBalance> calculateBalance(Map<Label, List<Transaction>> map) {
    List<AccountBalance> accountBalances = new ArrayList<>();
    for (Entry<Label, List<Transaction>> e : map.entrySet()) {
      BigDecimal balance = new BigDecimal(0);
      for (Transaction transaction : e.getValue()) {
        if (transaction.getFromAccount().hasLabel(e.getKey())) {
          balance = balance.add(transaction.getFromAmount().getBigDecimal());
        }
        if (transaction.getToAccount().hasLabel(e.getKey())) {
          balance = balance.add(transaction.getToAmount().getBigDecimal());
        }
      }
      accountBalances.add(new AccountBalance(e.getKey(), balance));
    }
    return accountBalances;
  }

  private Map<Label, List<Transaction>> organizeByLabels() {
    Map<Label, List<Transaction>> map = new HashMap<>();
    for (Transaction transaction : transactions) {
      transaction.getFromAccount().getLabels().stream()
          .forEach(label ->
              map.computeIfAbsent(label, x -> new ArrayList<>()).add(transaction));
      transaction.getToAccount().getLabels().stream()
          .forEach(label ->
              map.computeIfAbsent(label, x -> new ArrayList<>()).add(transaction));
    }
    return map;
  }
}
