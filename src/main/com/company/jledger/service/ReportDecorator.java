package com.company.jledger.service;

import com.company.jledger.model.AccountBalance;
import com.company.jledger.model.Label;
import com.company.jledger.model.Transaction;
import com.google.common.collect.Table;
import dnl.utils.text.table.GuavaTableModel;
import dnl.utils.text.table.TextTable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ReportDecorator {
  public void printTransactions(
      Map<Label, List<Transaction>> transactionMap,
      Label label) {
    Object[][] cells = transactionMap.get(label)
        .stream()
        .map(x -> x.getRow(label))
        .toArray(Object[][]::new);
    TextTable textTable = new TextTable(Transaction.getColumnNames(), cells);
    textTable.setSort(0);
    textTable.printTable();
  }

  public void printBalances(List<AccountBalance> accountBalances) {
    Object[][] cells = accountBalances
        .stream()
        .map(x-> new Object[]{x.getLabelNamespace(), x.getBigDecimal()})
        .toArray(Object[][]::new);
    TextTable textTable = new TextTable(AccountBalance.getColumnNames(), cells);
    textTable.setSort(0);
    textTable.printTable();
  }
}
