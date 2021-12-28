package com.ozgeek.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class TelegramBotConfiguration {

  @NotNull
  @JsonProperty
  private String botName;

  @NotNull
  @JsonProperty
  private String botToken;

  @NotNull
  @JsonProperty
  private String chatId;


  public String getBotName() {
    return botName;
  }

  public String getBotToken() {
    return botToken;
  }

  public String getChatId() {
    return chatId;
  }
}
