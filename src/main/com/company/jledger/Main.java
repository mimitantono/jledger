package com.company.jledger;

import com.company.jledger.model.Account;
import com.company.jledger.model.Currency;
import com.company.jledger.model.FailConversion;
import com.company.jledger.model.Label;
import com.company.jledger.service.ReportGenerator;
import com.company.jledger.service.TransactionImporter;
import com.company.jledger.utils.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class Main {

  public static void main(String[] args) throws IOException, URISyntaxException {
//    Preconditions.checkNotNull(args[0]);
    String fileName = "file:///home/mimi/Downloads/transaksjoner.csv";
    FileReader fileReader = new FileReader(fileName);
    TransactionImporter transactionImporter = new TransactionImporter(new Currency("NOK"),
        new SimpleDateFormat("dd.MM.yyyy"));
    List<FailConversion> failConversions = transactionImporter
        .readTransactions(fileReader.readFile());
    if (failConversions.size() > 0) {
      failConversions.forEach(log::error);
    }
    ReportGenerator reportGenerator = new ReportGenerator(transactionImporter.getTransactions());
    reportGenerator.generateReport();
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:Brukskonto"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:Sparekonto"));
    reportGenerator.listAccountTransactions(new Label("Liabilities:Credit:HouseLoan"));
    reportGenerator.listAccountTransactions(new Label("Liabilities:Credit:Komplett"));
    reportGenerator.listAccountTransactions(new Label("Liabilities:Credit:Remember"));
    reportGenerator.listAccountTransactions(new Label("Liabilities:Credit:Telia"));
    reportGenerator.listAccountTransactions(new Label("Liabilities:Credit:Trumf"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:Komplett:Sparekonto"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:USAIndeks"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:Spare50"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:GlobalIndeks"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:EuropaIndeks"));
    reportGenerator.listAccountTransactions(new Label("Assets:Bank:DNB:ASK"));

  }
}
