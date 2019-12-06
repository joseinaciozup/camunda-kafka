package br.com.itau.journey.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FetchAndLockRequest {

    private String workerId;

    private Integer maxTasks;

    private Boolean usePriority;

    private List<TopicRequest> topics;

}
