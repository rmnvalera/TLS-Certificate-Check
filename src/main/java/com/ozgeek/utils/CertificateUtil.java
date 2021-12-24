/*
 * Copyright 2020 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.ozgeek.utils;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.openssl.PEMReader;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CertificateUtil {
  public static KeyStore buildKeyStoreForPem(final String[] caCertificatePem) throws CertificateException
  {
    try {
      List<X509Certificate> certificates = getCertificate(caCertificatePem);

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null);
      if (certificates.isEmpty()) {
        return keyStore;
      }

      for (int i = 0; i < certificates.size(); i++) {
        X509Certificate     x509Certificate = certificates.get(i);
        final X509Principal principal       = PrincipalUtil.getSubjectX509Principal(x509Certificate);
        final Vector<?>     values          = principal.getValues(X509Name.CN);
        final String        alias           = String.format("%s - %d", values.get(0), i);
        keyStore.setCertificateEntry(alias, x509Certificate);
      }

      return keyStore;
    } catch (IOException | KeyStoreException ex) {
      throw new CertificateException(ex);
    } catch (NoSuchAlgorithmException ex) {
      throw new AssertionError(ex);
    }
  }

  public static List<X509Certificate> getCertificate(final String[] certificatePems) throws CertificateException {
    List<X509Certificate> x509CertificateList = new ArrayList<>();
    for (String certificate : certificatePems) {
      try (PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(certificate.getBytes())))) {
        X509Certificate x509Certificate = (X509Certificate) reader.readObject();
        x509CertificateList.add(x509Certificate);
      } catch (IOException e) {
        throw new CertificateException(e);
      }
    }
    return x509CertificateList;
  }
}
