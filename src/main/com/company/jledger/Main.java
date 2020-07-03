package com.company.jledger;

import com.company.jledger.model.Currency;
import com.company.jledger.model.FailConversion;
import com.company.jledger.model.Label;
import com.company.jledger.service.ReportGenerator;
import com.company.jledger.service.TransactionImporter;
import com.company.jledger.utils.FileReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.extern.log4j.Log4j;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Log4j
public class Main implements Callable<Integer> {

  @Parameters(index = "0", description = "The file")
  private File file;

  @Option(names = {"-l",
      "--list"}, description = "List transactions for an account e.g. -l Income:Salary")
  private String label;

  @Option(names = {"-b", "--balance"}, description = "Print balance of all accounts")
  private boolean printBalance;

  public static void main(String[] args) throws IOException, URISyntaxException {
    int exitCode = new CommandLine(new Main()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    FileReader fileReader = new FileReader(file);
    TransactionImporter transactionImporter = new TransactionImporter(new Currency("NOK"),
        new SimpleDateFormat("dd.MM.yyyy"));
    List<FailConversion> failConversions = transactionImporter
        .readTransactions(fileReader.readFile());
    if (failConversions.size() > 0) {
      failConversions.forEach(log::error);
      return -1;
    }
    ReportGenerator reportGenerator = new ReportGenerator(transactionImporter.getTransactions());
    if (printBalance) {
      reportGenerator.generateReport();
    }
    if (label != null) {
      reportGenerator.listAccountTransactions(new Label(label));
    }
    return 0;
  }
}
