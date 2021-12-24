package com.ozgeek;


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
    DomainCheckWorker domainCheckWorker = new DomainCheckWorker(configuration.getListCaCertificate());
    domainCheckWorker.getCertificate("https://im.dstarlab.com");
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
