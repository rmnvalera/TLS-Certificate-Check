package com.ozgeek.configurations;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ozgeek.Application;
import com.ozgeek.configurations.system.*;
import com.ozgeek.utils.SystemMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class BaseConfigurationFactory<T extends ConfigurationApp> {

  private static final ObjectMapper                mapper        = SystemMapper.getMapper();
  private static final JsonFactory                 parserFactory = new YAMLFactory();
  private static final ConfigurationSourceProvider provider      = new FileConfigurationSourceProvider();

  private static final ValidatorFactory factory   = Validation.buildDefaultValidatorFactory();
  private static final Validator        validator = factory.getValidator();

  private final Class<T> klass;

  public BaseConfigurationFactory(Class<T> klass) {
    this.klass = klass;
  }

  public T build(String path) throws ConfigurationException, IOException {
    try (InputStream input = provider.open(requireNonNull(path))) {
      final JsonNode node = mapper.readTree(createParser(input));

      if (node == null) {
        throw ConfigurationParsingException
                .builder("Configuration at " + path + " must not be empty")
                .build(path);
      }

      return build(node, path);
    } catch (JsonParseException e) {
      throw ConfigurationParsingException
              .builder("Malformed " + YAMLFactory.FORMAT_NAME_YAML)
              .setCause(e)
              .setLocation(e.getLocation())
              .setDetail(e.getMessage())
              .build(path);
    }
  }


  protected T build(JsonNode node, String path) throws IOException, ConfigurationException {
    try {
      final T config = mapper.readValue(new TreeTraversingParser(node), klass);
      validate(path, config);
      return config;
    } catch (UnrecognizedPropertyException e) {
      final List<String> properties = e.getKnownPropertyIds().stream()
                                       .map(Object::toString)
                                       .collect(Collectors.toList());
      throw ConfigurationParsingException.builder("Unrecognized field")
                                         .setFieldPath(e.getPath())
                                         .setLocation(e.getLocation())
                                         .addSuggestions(properties)
                                         .setSuggestionBase(e.getPropertyName())
                                         .setCause(e)
                                         .build(path);
    } catch (InvalidFormatException e) {
      final String sourceType = e.getValue().getClass().getSimpleName();
      final String targetType = e.getTargetType().getSimpleName();
      throw ConfigurationParsingException.builder("Incorrect type of value")
                                         .setDetail("is of type: " + sourceType + ", expected: " + targetType)
                                         .setLocation(e.getLocation())
                                         .setFieldPath(e.getPath())
                                         .setCause(e)
                                         .build(path);
    } catch (JsonMappingException e) {
      throw ConfigurationParsingException.builder("Failed to parse configuration")
                                         .setDetail(e.getMessage())
                                         .setFieldPath(e.getPath())
                                         .setLocation(e.getLocation())
                                         .setCause(e)
                                         .build(path);
    }
  }

  protected JsonParser createParser(InputStream input) throws IOException {
    return parserFactory.createParser(input);
  }

  private void validate(String path, T config) throws ConfigurationValidationException {
    if (validator != null) {
      final Set<ConstraintViolation<T>> violations = validator.validate(config);
      if (!violations.isEmpty()) {
        throw new ConfigurationValidationException(path, violations);
      }
    }
  }
}
