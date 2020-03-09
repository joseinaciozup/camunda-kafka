package br.com.itau.journey;

import br.com.itau.journey.consumer.ConsumerServiceKafka;
import br.com.itau.journey.consumer.ServiceRunner;
import br.com.itau.journey.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateJourneyService implements ConsumerServiceKafka<String> {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    private final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

    private Map<String, String> variables = new HashMap<>();

    public static void main(String[] args) {
        new ServiceRunner(CreateJourneyService::new).start(5);
    }

    public String getConsumerGroup() {
        return CreateJourneyService.class.getSimpleName();
    }

    public String getTopic() {
        return "create.journey";
    }

    public void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("------------------------------------------");
        System.out.println("Created Journey!");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try {
            Message message = record.value();
            RequestStartDTO requestStart = (RequestStartDTO) message.getPayload();
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(requestStart.getBpmnInstance(), getVariables(requestStart.getVariables()));

            if (requestStart.isSync()) {
                waitingProcessEnd(processInstance);
            }

            UUID id = UUID.randomUUID();
            Message messageEnd = new Message(new CorrelationId("End"), "End");
            dispatcher.send("end.journey", id.toString(), messageEnd.getId().continueWith(CreateJourneyService.class.getSimpleName()), requestStart.toString());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Email sent");
    }

    private void waitingProcessEnd(ProcessInstance processInstance) throws InterruptedException {
        boolean x = true;
        while (x) {
            Thread.sleep(5000);
            x = processEngine
                    .getRuntimeService()
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult() != null;
        }
    }

    private Map<String, Object> getVariables(VariablesStartDTO variablesStartDTO) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("time1", variablesStartDTO.getTimeService1());
        variables.put("time2", variablesStartDTO.getTimeService2());
        variables.put("time3", variablesStartDTO.getTimeService3());
        variables.put("directionA", variablesStartDTO.getDirectionA());
        variables.put("directionB", variablesStartDTO.getDirectionB());
        variables.put("valueDirection", 0);

        return variables;
    }


}
