package com.ozgeek.TelegramBot;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends ServiceCommand {

  StartCommand(String identifier, String description) {
    super(identifier, description);
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
    sendAnswer(absSender, chat.getId(),
               "Lets go! If you need help, you can press /help");
  }
}

