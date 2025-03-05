package com.wit.rest.kafka;

import com.wit.rest.model.OperationMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class OperationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OperationConsumer.class);

    /**
     * Consome mensagens do Kafka e mantém a rastreabilidade do requestId.
     *
     * @param record Registro Kafka contendo a mensagem e headers.
     */
    @KafkaListener(topics = "calc-requests", groupId = "calc-group")
    public void listen(ConsumerRecord<String, OperationMessage> record) {
        // Extrai o requestId dos headers da mensagem Kafka
        String requestId = null;
        if (record.headers().lastHeader("requestId") != null) {
            requestId = new String(record.headers().lastHeader("requestId").value(), StandardCharsets.UTF_8);
        }

        MDC.put("requestId", requestId); // Define o requestId no MDC para logging

        OperationMessage message = record.value();
        logger.info("UUID={} | Mensagem recebida via Kafka: Operação={} | a={} | b={} | Resultado={}",
                requestId, message.getOperation(), message.getA(), message.getB(), message.getResult());

        MDC.clear(); // Limpa o MDC após o processamento
    }
}
