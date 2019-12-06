package org.camunda.bpm.spring.boot.example.simple;

import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class DocumentService {

  private static String REGEX_ONLY_NUMBER = "[^\\d]";

  public String cleanDocument(String document) {
    return ofNullable(document).map(s -> s.replaceAll(REGEX_ONLY_NUMBER, "")).orElse(document);
  }
}
