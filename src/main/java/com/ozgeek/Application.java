package com.ozgeek;

import com.ozgeek.configurations.BaseConfigurationFactory;
import com.ozgeek.configurations.ConfigurationApp;
import com.ozgeek.utils.Generics;

import java.io.IOException;
import java.security.cert.CertificateException;

public class Application<T extends ConfigurationApp> {

  public void run(String... arguments) throws Exception {
    BaseConfigurationFactory<T> configurationFactory = new BaseConfigurationFactory<T>(getConfigurationClass());
    T config = configurationFactory.build("config/config.yml");
    run(config);
  }

  protected Class<T> getConfigurationClass() {
    return Generics.getTypeParameter(getClass(), ConfigurationApp.class);
  }
  protected void run(T config) throws IOException, CertificateException {
  }
}
