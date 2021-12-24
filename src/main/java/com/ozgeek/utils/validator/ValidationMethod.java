package com.ozgeek.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = MethodValidator.class)
@Documented
public @interface ValidationMethod {
  String message() default "is not valid";

  Class<?>[] groups() default {};

  @SuppressWarnings("UnusedDeclaration") Class<? extends Payload>[] payload() default { };
}
