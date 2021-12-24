package com.ozgeek.tls;

import com.ozgeek.filedb.FileDbManager;
import com.ozgeek.utils.CertificateUtil;
import com.ozgeek.utils.Util;
import nl.altindag.ssl.SSLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DomainCheckWorker {

  Logger logger = LoggerFactory.getLogger(DomainCheckWorker.class);

  private final SSLContext    sslContext;
  private final FileDbManager dbManager;

  private final long sleepInterval;

  public DomainCheckWorker(FileDbManager dbManager, String[] pemCaCert) throws CertificateException {
    this(dbManager, pemCaCert, TimeUnit.DAYS.toMillis(1));
  }

  public DomainCheckWorker(FileDbManager dbManager, String[] pemCaCert, long sleepInterval)
          throws CertificateException
  {
    this.sslContext    = initializeClient(pemCaCert);
    this.dbManager     = dbManager;
    this.sleepInterval = sleepInterval;
  }

  public void run() {
    new Thread(() -> {
      while (true) {
        try {
          List<URL> hosts = dbManager.readAll();
          hosts.stream()
               .map(URL::toString)
               .forEach(this::getCertificate);
        } catch (IOException e) {
          e.printStackTrace();
        }

        Util.sleep(sleepInterval);
      }
    }).start();
  }


  private static SSLContext initializeClient(String[] pemCaCert)
          throws CertificateException
  {
    KeyStore trustStore = CertificateUtil.buildKeyStoreForPem(pemCaCert);
    return SSLFactory.builder()
                     .withDefaultTrustMaterial() // uses JDK trust store
                     .withTrustMaterial(trustStore)
                     .build()
                     .getSslContext();

  }

  public void getCertificate(String host) {
    try {
      URL                destinationURL   = new URL(host);
      HttpsURLConnection conn             = (HttpsURLConnection) destinationURL.openConnection();
      SSLSocketFactory   sslSocketFactory = sslContext.getSocketFactory();
      conn.setSSLSocketFactory(sslSocketFactory);
      conn.connect();
      Certificate[] certs = conn.getServerCertificates();

      for (Certificate certificate : certs) {
        Date dateExpired = ((X509Certificate) certificate).getNotAfter();
        if (dateExpired.getTime() - TimeUnit.DAYS.toMillis(7) <= System.currentTimeMillis()) {
          logger.warn(String.format("Domain certificate: %s, expires %s", destinationURL.getHost(), dateExpired));
        }
      }
    } catch (IOException e) {
      logger.warn("TLS exception: " + e.getMessage());
    }
  }

}
