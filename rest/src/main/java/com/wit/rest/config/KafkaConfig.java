package com.wit.rest.config;

import com.wit.rest.model.OperationMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do Apache Kafka para producers e consumers.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    // Endereço do servidor Kafka (modifique conforme necessário)
    private final String bootstrapAddress = "kafka:9092";

    /**
     * Configuração da fábrica de produtores para Kafka.
     *
     * @return Uma instância de {@link ProducerFactory}
     */
    @Bean
    public ProducerFactory<String, OperationMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Cria um template Kafka para envio de mensagens.
     *
     * @return Instância de {@link KafkaTemplate}
     */
    @Bean
    public KafkaTemplate<String, OperationMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configuração da fábrica de consumidores para Kafka.
     *
     * @return Instância de {@link ConsumerFactory}
     */
    @Bean
    public ConsumerFactory<String, OperationMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "calc-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonDeserializer.class);
        props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES,
                "com.wit.rest.model");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Configuração da fábrica de containers para consumidores Kafka.
     *
     * @return Instância de {@link ConcurrentKafkaListenerContainerFactory}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OperationMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OperationMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
