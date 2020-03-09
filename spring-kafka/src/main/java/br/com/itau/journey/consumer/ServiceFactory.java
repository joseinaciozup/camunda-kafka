package br.com.itau.journey.consumer;

public interface ServiceFactory<T> {
    ConsumerServiceKafka<T> create() throws Exception;
}
