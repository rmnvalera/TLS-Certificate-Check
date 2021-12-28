package com.ozgeek.TelegramBot;

import com.ozgeek.filedb.FileDbManager;
import com.ozgeek.utils.SystemMapper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ShowDomainsCommand extends ServiceCommand  {

  private final FileDbManager dbManager;

  public ShowDomainsCommand(String identifier, String description, FileDbManager dbManager) {
    super(identifier, description);

    this.dbManager = dbManager;
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] paramethers) {

    try {
      List<URL> urls = dbManager.readAll();

      StringBuilder answer = new StringBuilder();

      urls.stream()
          .map(URL::toString)
          .map(urlStr -> urlStr + System.lineSeparator())
          .forEach(answer::append);

      sendAnswer(absSender, chat.getId(), answer.toString());

    } catch (IOException e) {
      e.printStackTrace();

      sendAnswer(absSender, chat.getId(), "Error handle: " + e.getMessage());
    }

  }
}
