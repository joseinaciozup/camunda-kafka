package br.com.itau.journey.kafka;

import br.com.itau.journey.domain.KafkaExternalTask;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaProducerConfig {

    @Bean
    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "external-task-journey");


        return props;
    }

    @Bean
    public ProducerFactory<String, Message<KafkaExternalTask>> producerFactory() {
        DefaultKafkaProducerFactory<String, Message<KafkaExternalTask>> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(new ObjectMapper().registerModule(new JavaTimeModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, Message<KafkaExternalTask>> kafkaTemplate() {
        return new KafkaTemplate(this.producerFactory());
    }
}
