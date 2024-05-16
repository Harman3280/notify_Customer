package com.main.notifycustomer.controller;

import com.main.notifycustomer.entity.Customer;
import com.main.notifycustomer.service.NotifyMeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyMeController {
    Logger logger = LoggerFactory.getLogger(NotifyMeController.class);

    @Autowired
    private NotifyMeService notifyMeService;


    @PostMapping("/productInventory/v1/notifyMe/{productId}")
    public ResponseEntity<Void> execute (@RequestBody Customer customer, @PathVariable String productId)
    {
        logger.info("Notify Customer for Product : " + productId);
        try{
            notifyMeService.notifyCustomer(customer.getCustomerId(), productId);

            return ResponseEntity.status(201).build();

        }catch (Exception e) {

            throw e;
        }

    }
}
