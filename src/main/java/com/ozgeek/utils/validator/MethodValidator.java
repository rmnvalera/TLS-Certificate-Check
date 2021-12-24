package com.ozgeek.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MethodValidator implements ConstraintValidator<ValidationMethod, Boolean> {
  @Override
  public void initialize(ValidationMethod constraintAnnotation) {

  }

  @Override
  public boolean isValid(Boolean value, ConstraintValidatorContext context) {
    return (value == null) || value;
  }
}