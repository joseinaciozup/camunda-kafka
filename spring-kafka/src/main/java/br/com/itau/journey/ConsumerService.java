package br.com.itau.journey;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import br.com.itau.journey.feign.CamundaExternalTaskApi;
import br.com.itau.journey.feign.CompleteTaskRequest;
import br.com.itau.journey.feign.FetchAndLockRequest;
import br.com.itau.journey.feign.FetchAndLockResponse;
import br.com.itau.journey.feign.TopicRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerService {

    public static final String TEST_EXTERNAL_WORKER_ID = "testExternalWorkerId";
    private CamundaExternalTaskApi camundaExternalTaskApi;

    @Autowired
    public ConsumerService(CamundaExternalTaskApi camundaExternalTaskApi) {
        this.camundaExternalTaskApi = camundaExternalTaskApi;
    }


    /*@KafkaListener(
            id = "externalTaskTestProcessor",
            topics = "${externaltask.topic.name}",
            containerFactory = "kafkaListenerContainerFactory")
    public void greetingListener(KafkaExternalTask greeting, Acknowledgment ack) {

        Mono<KafkaExternalTask> kafkaFlux = Mono.just(greeting).subscribeOn(Schedulers.elastic());
        kafkaFlux.subscribe(data -> {
                    try {
                        log.info("Kafka-Audit: " + data.toString());
                        List<FetchAndLockResponse> fetchAndLockResponses = camundaExternalTaskApi.fetchAndLock(FetchAndLockRequest.builder()
                                .workerId(TEST_EXTERNAL_WORKER_ID)
                                .maxTasks(1)
                                .usePriority(true)
                                .topics(Arrays.asList(TopicRequest.builder()
                                        .topicName(data.getCamundaTopic())
                                        .lockDuration(60000L)
                                        .build()))
                                .build());
                        fetchAndLockResponses.get(0);
                        ofNullable(fetchAndLockResponses).ifPresent(lockResponses -> handleTasks(lockResponses));
                    } catch (BaseIntegrationException e) {
                        log.error("shapultepeque {}",e.getMessage());
                    }
                },
                getError(),
                getRunnable(ack));
    }*/

    @KafkaListener(
            id = "externalTaskTestProcessor",
            topics = "${externaltask.topic.name}",
            containerFactory = "kafkaListenerContainerFactory")
    public void greetingListener(KafkaExternalTask data, Acknowledgment ack) {

        log.info("Kafka-Audit: " + data.toString());
        List<FetchAndLockResponse> fetchAndLockResponses = camundaExternalTaskApi.fetchAndLock(FetchAndLockRequest.builder()
                .workerId(TEST_EXTERNAL_WORKER_ID)
                .maxTasks(1)
                .usePriority(true)
                .topics(Arrays.asList(TopicRequest.builder()
                        .topicName(data.getCamundaTopic())
                        .lockDuration(60000L)
                        .build()))
                .build());
        fetchAndLockResponses.get(0);
        fetchAndLockResponses.forEach(processFetchAndLockResponseConsumer());
        getRunnable(ack);
    }


    @NotNull
    private Consumer<Throwable> getError() {
        return error -> {
            log.error("Error", error);
            throw new RuntimeException("owsa");
        };
    }

    /*@NotNull
    private Runnable getRunnable(Acknowledgment ack) {
        return () -> {
            log.info("##################### acknowledge");
            ack.acknowledge();
        };
    }*/

    private void getRunnable(Acknowledgment ack) {
        log.info("##################### acknowledge");
        ack.acknowledge();
    }

    private void handleTasks(List<FetchAndLockResponse> lockResponses) {
        log.info("ExternalTasks List SIZE[{}]", lockResponses.size());
        if (lockResponses.size() == 0) throw new RuntimeException("Try again!!!");
        lockResponses.forEach(processFetchAndLockResponseConsumer());
    }

    @NotNull
    private Consumer<FetchAndLockResponse> processFetchAndLockResponseConsumer() {
        return fetchAndLockResponse -> completeTask(fetchAndLockResponse);
    }

    private void completeTask(FetchAndLockResponse fetchAndLockResponse) {
        log.info("COMPLETING[{}] EXTERNAL TASK", fetchAndLockResponse.getId());
        camundaExternalTaskApi.complete(fetchAndLockResponse.getId(), CompleteTaskRequest.builder()
                .workerId(TEST_EXTERNAL_WORKER_ID)
                .build());
    }


}
