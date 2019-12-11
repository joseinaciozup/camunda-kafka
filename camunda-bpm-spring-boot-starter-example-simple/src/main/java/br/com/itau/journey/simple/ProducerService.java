package br.com.itau.journey.simple;

import br.com.itau.journey.domain.KafkaExternalTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class ProducerService {

    private KafkaTemplate<String, Message<KafkaExternalTask>> template;

    @Autowired
    public ProducerService(KafkaTemplate<String, Message<KafkaExternalTask>> template) {
        this.template = template;
    }

    @Async
    public void sendToKafka(Message<KafkaExternalTask> kafkaExternalTaskMessage) {

        ListenableFuture<SendResult<String, Message<KafkaExternalTask>>> future = template.send(kafkaExternalTaskMessage);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Message<KafkaExternalTask>>>() {

            @Override
            public void onSuccess(SendResult<String, Message<KafkaExternalTask>> result) {
                log.info(ToStringBuilder.reflectionToString(result));
            }

            @Override
            public void onFailure(Throwable exception) {
                log.error(exception.getMessage());
            }
        });
    }

}
