package com.main.notifycustomer.service;

import com.main.notifycustomer.entity.NotifyCustomersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotifyMeService {

    Logger logger = LoggerFactory.getLogger(NotifyMeService.class);

    @Autowired
    @Qualifier("notifyCustomerTemplate")
    private CouchbaseTemplate notifyCustomerTemplate;

    public void notifyCustomer(String customerId , String productId)
    {
        logger.info("Customer Adding for notification : " + customerId);

        Optional<NotifyCustomersDTO> optionalNotifyCustomersDTO = Optional.ofNullable(notifyCustomerTemplate.findById(NotifyCustomersDTO.class).one(productId));
        if (optionalNotifyCustomersDTO.isPresent()) {
            NotifyCustomersDTO notifyCustomersDtoResponse = optionalNotifyCustomersDTO.get();
            notifyCustomersDtoResponse.getCustomersData().add(customerId);

            notifyCustomerTemplate.save(notifyCustomersDtoResponse);


        }
        else
        {

            NotifyCustomersDTO notifyCustomersDtoResponse = new NotifyCustomersDTO();
            notifyCustomersDtoResponse.setId(productId);
            notifyCustomersDtoResponse.getCustomersData().add(customerId);
            notifyCustomerTemplate.save(notifyCustomersDtoResponse);
        }
    }
}
