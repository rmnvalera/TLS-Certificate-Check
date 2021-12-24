package com.ozgeek.filedb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileDbManager {

  Logger logger = LoggerFactory.getLogger(FileDbManager.class);

  private final Path pathToFileDb;

  public FileDbManager(String pathToFileDb) {
    this.pathToFileDb = Paths.get(pathToFileDb);
  }

  public List<URL> readAll() throws IOException {
    List<URL> urlList = new ArrayList<>();
    try (Stream<String> stream = Files.lines(pathToFileDb)) {
      stream.filter(s -> !s.isBlank())
            .map(this::parseStringToUrl)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(urlList::add);

    }
    return urlList;
  }

  public void write(String host) throws IOException {
    Optional<URL> url = parseStringToUrl(host);
    if (url.isPresent()) {
        Files.writeString(pathToFileDb, System.lineSeparator() + host, CREATE, APPEND);
    }
  }

  public void delete(String host) throws IOException {
      List<String> out = Files.lines(pathToFileDb)
                              .filter(line -> !line.contains(host))
                              .filter(line -> !line.isEmpty())
                              .collect(Collectors.toList());
      Files.write(pathToFileDb, out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
  }


  private Optional<URL> parseStringToUrl(String host) {
    if (host.contains("http://")) {
      logger.warn("url: " + host + " convert protocol to https");
      host = host.replace("http", "https");
    }
    if (!host.contains("https://")) {
      host = "https://" + host;
      new StringBuilder("https://").append(host);
    }

    try {
      return Optional.of(new URL(host));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}

