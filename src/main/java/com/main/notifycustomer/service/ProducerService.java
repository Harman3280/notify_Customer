package com.main.notifycustomer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplateSuperHero;


    public void publish(T productInventory, String key) {
        logger.info("Publishing ProductInventory Event to Kafka Topic:: {}", productInventory);
        kafkaTemplateSuperHero.send(topic, key, productInventory);
    }
}
