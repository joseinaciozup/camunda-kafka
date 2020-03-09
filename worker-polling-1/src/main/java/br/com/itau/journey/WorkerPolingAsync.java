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
    public Executor executorStarter(@Value("${camunda.worker-polling-thread}") int qtdThread,
                                    @Value("${camunda.worker-max-queue-size}") int maxQueue) {
        return new ThreadPoolExecutor(qtdThread, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(maxQueue * qtdThread));
    }

    @Bean(name = "workerPollingAsyncThreads")
    public Executor executorThreads(@Value("${camunda.worker-taks-thread}") int qtdThread,
                                    @Value("${camunda.worker-max-queue-size}") int maxQueue) {
        return new ThreadPoolExecutor(qtdThread, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(maxQueue * qtdThread));
    }
}
