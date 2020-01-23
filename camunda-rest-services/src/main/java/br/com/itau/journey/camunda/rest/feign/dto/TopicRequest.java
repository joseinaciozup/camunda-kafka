package br.com.itau.journey.camunda.rest.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TopicRequest {

    private String topicName;

    private Long lockDuration;

}
