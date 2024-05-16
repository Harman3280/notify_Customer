package com.main.notifycustomer.controller;

import com.main.notifycustomer.entity.ProductInventroy;
import com.main.notifycustomer.service.AddProductToInventoryService;
import com.main.notifycustomer.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddProductToInventoryController {

    Logger logger = LoggerFactory.getLogger(AddProductToInventoryController.class);

    @Autowired
    AddProductToInventoryService addToInventoryService;

    @Autowired
    private ProducerService producerService;

    @PostMapping("/productInventory/v1/addToInventory")
    public ResponseEntity<ProductInventroy> execute (@RequestBody ProductInventroy productInventroy)
    {
        logger.info("product request for cart : "+productInventroy);
        try{

            ProductInventroy productInventroy_response = addToInventoryService.addToCart(productInventroy);

            //once product is added to cart, publish message to kafka topic
            producerService.publish(productInventroy_response, productInventroy_response.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(productInventroy_response);
        }
        catch(Exception e)
        {
            throw e;
        }

    }

}
