package com.ozgeek.TelegramBot;

import com.ozgeek.filedb.FileDbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

public class AddDomainCommand extends ServiceCommand {

  private static final Logger logger = LoggerFactory.getLogger(AddDomainCommand.class);

  private final FileDbManager dbManager;

  AddDomainCommand(String identifier, String description, FileDbManager dbManager) {
    super(identifier, description);
    this.dbManager = dbManager;
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] parameters) {
    if (parameters.length != 1) {
      sendAnswer(absSender, chat.getId(), "Error: one domain must be passed as a command parameter");
      return;
    }
    String domain = parameters[0];

    try {
      dbManager.write(domain);
      sendAnswer(absSender, chat.getId(), String.format("Done: [%s will be added]", domain));
      logger.info(String.format("%s will be added", domain));
    } catch (IOException e) {
      e.printStackTrace();

      sendAnswer(absSender, chat.getId(), "Error handle: " + e.getMessage());
    }
  }
}
