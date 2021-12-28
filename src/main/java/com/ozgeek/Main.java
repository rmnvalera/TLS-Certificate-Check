package com.ozgeek;


import com.ozgeek.TelegramBot.TelegramBot;
import com.ozgeek.filedb.FileDbManager;
import com.ozgeek.tls.DomainCheckWorker;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.security.Security;
import java.security.cert.CertificateException;

public class Main extends Application<Configuration> {

  Logger logger = LoggerFactory.getLogger(Main.class);

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Override
  public void run(Configuration configuration) throws CertificateException, TelegramApiException {

    FileDbManager dbManager = new FileDbManager(configuration.getPathToFileDB());
//    List<URL>     hosts     = dbManager.readAll();
//    dbManager.write("docs.oracle.com");
//    dbManager.delete("docs.oracle.com");

    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    TelegramBot bot = new TelegramBot(
            configuration.getTelegramBot().getBotName(),
            configuration.getTelegramBot().getBotToken(),
            configuration.getTelegramBot().getChatId(),
            dbManager
    );
    botsApi.registerBot(bot);
    bot.sendMessageToGroup("Let's get starting: /start");


    DomainCheckWorker domainCheckWorker = new DomainCheckWorker(dbManager, configuration.getListCaCertificate(), bot);
    domainCheckWorker.run();
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
