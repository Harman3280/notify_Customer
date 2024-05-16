package com.main.notifycustomer.service;

import com.main.notifycustomer.entity.NotifyCustomersDTO;
import com.main.notifycustomer.entity.ProductInventroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("notifyCustomerTemplate")
    private CouchbaseTemplate notifyCustomerTemplate;

    @Autowired
    private JavaMailSender emailSender;


    @KafkaListener(topics = {"${spring.kafka.topic}"}, containerFactory = "kafkaListenerJsonFactory", groupId = "notifyConsumer")
    public void consumer(ProductInventroy productInventory) {
        String id = productInventory.getId();
        logger.info("Consumed ProductInventory Message from the kafka Topic with key :: {}", id);

        Optional<NotifyCustomersDTO> optionalNotifyCustomersDTO = Optional.ofNullable(notifyCustomerTemplate.findById(NotifyCustomersDTO.class).one(id));
        if (optionalNotifyCustomersDTO.isPresent()) {
            NotifyCustomersDTO notifyCustomersDtoResponse = optionalNotifyCustomersDTO.get();
            logger.info("Customer Data found for id: " + id + " :: " + notifyCustomersDtoResponse);

            List<String> customerEmails = notifyCustomersDtoResponse.getCustomersData();
            for(String email : customerEmails) {

                String text = "Dear "+ email + "Product you added in NotifyMe " + productInventory.getTitle() + "with" + productInventory.getSpecification() + " is Live!!";
                String subject = productInventory.getTitle() +" Product is Live!!";
                sendEmail(email, subject, text);
            }


        } else {
            logger.error("No Customer Data found for id: " + id);
        }




    }
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}