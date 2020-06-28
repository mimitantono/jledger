package com.company.jledger.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileReader {

  private final String fileName;

  public List<String> readFile() throws IOException, URISyntaxException {
    return Files.readAllLines(Paths.get(new URI(fileName)));
  }
}
