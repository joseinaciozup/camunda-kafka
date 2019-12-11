package br.com.itau.journey.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExternalTaskAccessInfo {

    private List<String> kafkaTopics;

    private KafkaExternalTask kafkaExternalTask;

}
