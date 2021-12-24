package com.ozgeek.configurations.system;

import com.google.common.collect.ImmutableSet;
import com.ozgeek.utils.validator.ConstraintViolations;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ConfigurationValidationException extends ConfigurationException {
  private static final long serialVersionUID = 5325162099634227047L;

  private final ImmutableSet<ConstraintViolation<?>> constraintViolations;

  public <T> ConfigurationValidationException(String path, Set<ConstraintViolation<T>> errors) {
    super(path, ConstraintViolations.format(errors));
    this.constraintViolations = ConstraintViolations.copyOf(errors);
  }

}