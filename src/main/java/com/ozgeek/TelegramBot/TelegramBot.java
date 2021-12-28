package com.ozgeek.TelegramBot;

import com.ozgeek.filedb.FileDbManager;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingCommandBot {

  private final String BOT_NAME;
  private final String BOT_TOKEN;
  private final String CHAT_ID;

  private static final String START_COMMAND  = "start";
  private static final String HELP_COMMAND   = "help";
  private static final String ADD_COMMAND    = "add";
  private static final String DELETE_COMMAND = "delete";
  private static final String SHOW_COMMAND   = "show";

  private static final String NON_TEXT_ANSWER = "Sorry, I don't understand you, maybe this will help your /help";

  public TelegramBot(String BOT_NAME, String BOT_TOKEN, String CHAT_ID, FileDbManager dbManager) {
    this.BOT_NAME  = BOT_NAME;
    this.BOT_TOKEN = BOT_TOKEN;
    this.CHAT_ID   = CHAT_ID;

    register(new StartCommand(START_COMMAND, "Start bot ..."));
    register(new ShowDomainsCommand(SHOW_COMMAND, "Show all domains, that can check. Use /show for getting list of domains", dbManager));
    register(new AddDomainCommand(ADD_COMMAND, "Add domains command. Use /add [domain] for add domain to list", dbManager));
    register(new DeleteDomainCommand(DELETE_COMMAND, "Delete domain command. Use /delete [domain] for deleting domain from list", dbManager));
    register(new HelpCommand());

  }


  @Override
  public String getBotUsername() {
    return BOT_NAME;
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }

  @Override
  public void processNonCommandUpdate(Update update) {
    Message msg    = update.getMessage();
    Long    chatId = msg.getChatId();

    setAnswer(chatId, NON_TEXT_ANSWER);
  }

  public void sendMessageToGroup(String text) {
    this.sendMessage(CHAT_ID, text);
  }

  private void setAnswer(Long chatId, String text) {
    this.sendMessage(chatId.toString(), text);
  }

  private void sendMessage(String chatId, String text) {
    SendMessage answer = new SendMessage();
    answer.setText(text);
    answer.setChatId(chatId);
    answer.setReplyMarkup(getSettingsKeyboard());
    try {
      execute(answer);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private static ReplyKeyboardMarkup getSettingsKeyboard() {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);

    List<KeyboardRow> keyboard         = new ArrayList<>();
    KeyboardRow       keyboardFirstRow = new KeyboardRow();
    keyboardFirstRow.add(HELP_COMMAND);
    keyboardFirstRow.add(START_COMMAND);
    KeyboardRow keyboardSecondRow = new KeyboardRow();
    keyboardSecondRow.add(SHOW_COMMAND);
    keyboard.add(keyboardFirstRow);
    keyboard.add(keyboardSecondRow);
    replyKeyboardMarkup.setKeyboard(keyboard);

    return replyKeyboardMarkup;
  }
}
