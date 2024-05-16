package com.main.notifycustomer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

public class NotifyCustomerCouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${values.couchbase.host}")
    private String connectionString;

    @Value("${values.couchbase.username}")
    private String username;

    @Value("${values.couchbase.password}")
    private String password;

    @Value("${values.couchbase.Buckets.notify_Customer.bucketName}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

}
