package com.ozgeek.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Person {

  @JsonProperty
  @NotNull
  private String name;

  @JsonProperty
  @Min(0)
  private int age;

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}
