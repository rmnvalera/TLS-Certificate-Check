package com.ozgeek;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozgeek.configurations.ConfigurationApp;
import com.ozgeek.configurations.Person;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Configuration implements ConfigurationApp {

  @JsonProperty
  @NotNull
  private List<String> listCaCertificate;

  @JsonProperty
  @Valid
  private Person person;


  public String[] getListCaCertificate() {
    return listCaCertificate.toArray(new String[0]);
  }

  public Person getPerson() {
    return person;
  }
}
