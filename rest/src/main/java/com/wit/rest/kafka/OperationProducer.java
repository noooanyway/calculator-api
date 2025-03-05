package com.wit.rest.kafka;

import com.wit.rest.model.OperationMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OperationProducer {

    private static final String TOPIC = "calc-requests";

    @Autowired
    private KafkaTemplate<String, OperationMessage> kafkaTemplate;

    /**
     * Envia a operação para o tópico Kafka com propagação de requestId.
     *
     * @param message   A mensagem contendo a operação matemática.
     * @param requestId O identificador único da requisição.
     */
    public void sendOperation(OperationMessage message, String requestId) {
        MDC.put("requestId", requestId); // Define o requestId no MDC

        // Cria um registro Kafka com headers personalizados
        ProducerRecord<String, OperationMessage> record = new ProducerRecord<>(TOPIC, message);
        record.headers().add("requestId", requestId.getBytes());

        kafkaTemplate.send(record);
    }
}
