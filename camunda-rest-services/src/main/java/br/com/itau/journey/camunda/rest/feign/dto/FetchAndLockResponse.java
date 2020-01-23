package br.com.itau.journey.camunda.rest.feign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FetchAndLockResponse {

    private String activityId;
    private String activityInstanceId;
    private String errorMessage;
    private String errorDetails;
    private String executionId;
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'.'SSSZ")
    private LocalDateTime lockExpirationTime;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processInstanceId;
    private Boolean suspended;
    private String workerId;
    private String topicName;
    private String tenantId;
    private Map<String, Object> variables;
    private Integer priority;
    private String businessKey;

}
