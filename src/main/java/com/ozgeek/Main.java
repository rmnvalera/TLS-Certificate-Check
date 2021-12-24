package com.ozgeek;


import com.ozgeek.filedb.FileDbManager;
import com.ozgeek.tls.DomainCheckWorker;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;
import java.security.cert.CertificateException;

public class Main extends Application<Configuration>{

  Logger logger = LoggerFactory.getLogger(Main.class);

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Override
  public void run(Configuration configuration) throws CertificateException {

    FileDbManager dbManager = new FileDbManager(configuration.getPathToFileDB());
//    List<URL>     hosts     = dbManager.readAll();
//    dbManager.write("docs.oracle.com");
//    dbManager.delete("docs.oracle.com");

    DomainCheckWorker domainCheckWorker = new DomainCheckWorker(dbManager, configuration.getListCaCertificate());
    domainCheckWorker.run();
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
