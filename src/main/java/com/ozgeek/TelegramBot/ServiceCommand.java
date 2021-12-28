package com.ozgeek.TelegramBot;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class ServiceCommand extends BotCommand {

  ServiceCommand(String identifier, String description) {
    super(identifier, description);
  }

  void sendAnswer(AbsSender absSender, Long chatId, String text) {
    SendMessage message = new SendMessage();
    message.enableMarkdown(true);
    message.setChatId(chatId.toString());
    message.setText(text);
    message.disableWebPagePreview();
    try {
      absSender.execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

}
