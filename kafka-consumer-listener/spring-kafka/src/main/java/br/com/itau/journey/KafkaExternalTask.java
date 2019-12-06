package br.com.itau.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class KafkaExternalTask {

    private String processInstanceId;
    private String activityInstanceId;
    private String currentActivityId;
    private String camundaTopic;

}
