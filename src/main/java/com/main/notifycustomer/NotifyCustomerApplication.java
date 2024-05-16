package com.main.notifycustomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@SpringBootApplication
@EnableCouchbaseRepositories
public class NotifyCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyCustomerApplication.class, args);
    }

}
