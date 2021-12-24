package com.ozgeek.tls;

import com.ozgeek.utils.CertificateUtil;
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
import java.util.concurrent.TimeUnit;

public class DomainCheckWorker {

  Logger logger = LoggerFactory.getLogger(DomainCheckWorker.class);

  private final SSLContext sslContext;

  public DomainCheckWorker(String[] pemCaCert) throws CertificateException {
    this.sslContext = initializeClient(pemCaCert);
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
          logger.warn(String.format("Domain certificate: %s, expires %s", destinationURL.getHost(), dateExpired.toString()));
        }
      }
    } catch (IOException e) {
      logger.warn("TLS exception: " + e.getMessage());
    }
  }

}
