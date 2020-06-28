package com.company.jledger.service;

import com.company.jledger.model.AccountBalance;
import com.company.jledger.model.Label;
import com.company.jledger.model.Transaction;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
public class ReportGenerator {

  private final List<Transaction> transactions;

  public void generateReport() {
    Map<Label, List<Transaction>> map = organizeByLabels();
    List<AccountBalance> accountBalances = calculateBalance(map);
    accountBalances.stream().sorted(Comparator.comparing(AccountBalance::getLabelNamespace))
        .forEach(x-> System.out.println(x));
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
