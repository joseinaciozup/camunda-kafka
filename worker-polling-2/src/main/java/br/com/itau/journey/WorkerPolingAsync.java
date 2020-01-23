package br.com.itau.journey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@EnableAsync
public class WorkerPolingAsync {

    @Bean(name = "workerPollingAsyncStarter")
    public Executor executorStarter(@Value("${camunda.worker-thread}") int qtdThread,
                             @Value("${camunda.worker-max-queue-size}") int maxQueueSize) {
        return new ThreadPoolExecutor(qtdThread, qtdThread, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(maxQueueSize));
    }
}
