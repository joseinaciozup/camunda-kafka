package br.com.itau.journey.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessStartTask implements JavaDelegate {

    private static Logger logger = LoggerFactory.getLogger(ProcessStartTask.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        logger.info("Start processing...");
    }
}
