package com.main.notifycustomer.service;

import com.main.notifycustomer.constant.ParameterConstants;
import com.main.notifycustomer.entity.ProductInventroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Random;


/*
 this class is adding Product to Cart and publishing message in kafka topic to notify customer
 */
@Service
public class AddProductToInventoryService {

    Logger logger = LoggerFactory.getLogger(AddProductToInventoryService.class);

    @Autowired
    @Qualifier("inventoryTemplate")
    private CouchbaseTemplate inventoryTemplate;

    @Value("${values.couchbase.Buckets.inventory.keyLength}")
    private int keyLength;

    public ProductInventroy addToCart(ProductInventroy productInventroy)
    {

        int key = generateRandomNumber(keyLength);

        String value = ParameterConstants.PRODUCT + key;
        productInventroy.setId(value);
        productInventroy.setUpdate_time(OffsetDateTime.now().toString());

        ProductInventroy productInventroy_response = inventoryTemplate.save(productInventroy);


        logger.info("Product Added to Cart Successfully  : "+productInventroy);
        return productInventroy_response;
    }


    private static int generateRandomNumber(int numDigits) {
        Random random = new Random();
        int minValue = (int) Math.pow(10, numDigits - 1); // Minimum value with numDigits
        int maxValue = (int) Math.pow(10, numDigits) - 1; // Maximum value with numDigits
        return random.nextInt(maxValue - minValue) + minValue;
    }
}
