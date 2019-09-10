package com.coelho.services.impl;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.models.Sale;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.ProducerRecord;

@ApplicationScoped
public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String KAFKA_TOPIC = "%s-api-sales";

    public void process(Sale sale) throws JsonProcessingException {
        KafkaConfiguration.createProducer().send(
                new ProducerRecord<>(buildTopicName(sale.getCustomerId().toString()), buildMessage(sale))
        );
    }

    private String buildTopicName(final String customerId) {
        return String.format(KAFKA_TOPIC, customerId);
    }

    private KafkaMessage buildMessage(Sale sale) throws JsonProcessingException {
        LOGGER.log(Level.INFO, "Sending to KAFKA " + sale.toString());

        return KafkaMessage.builder().payload(objectMapper.writeValueAsString(sale)).build();
    }
}