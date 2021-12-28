package com.ozgeek;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozgeek.configurations.ConfigurationApp;
import com.ozgeek.configurations.TelegramBotConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Configuration implements ConfigurationApp {

  @JsonProperty
  @NotNull
  private List<String> listCaCertificate;

  @JsonProperty
  @NotNull
  private String pathToFileDB;

  @JsonProperty
  @Valid
  @NotNull
  private TelegramBotConfiguration telegramBot;


  public String[] getListCaCertificate() {
    return listCaCertificate.toArray(new String[0]);
  }

  public String getPathToFileDB() {
    return pathToFileDB;
  }

  public TelegramBotConfiguration getTelegramBot() {
    return telegramBot;
  }
}
