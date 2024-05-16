package com.main.notifycustomer.config;

/*
will help to connect with existing couchbase buckets
 */

import com.couchbase.client.core.error.BucketExistsException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseMappingContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class CouchbaseConfig {

    private Cluster couchbaseCluster;

    @Value("${values.couchbase.Buckets.inventory.bucketName}")
    private String inventoryBucketName;

    @Value("${values.couchbase.Buckets.notify_Customer.bucketName}")
    private String notifyCustomerBucketName;

    @Value("${values.couchbase.host}")
    private String connectionString;

    @Value("${values.couchbase.username}")
    private String username;

    @Value("${values.couchbase.password}")
    private String password;

    /*
        in POSTConstruct, first establish the connection with the couchbase cluster and then create the bucket if it does not exist
     */

    @PostConstruct
    public void init() {
        ClusterEnvironment env = ClusterEnvironment.builder().build();
        couchbaseCluster = Cluster.connect(connectionString, ClusterOptions.clusterOptions(username, password).environment(env));

        createBucketIfNotExists(inventoryBucketName);
        createBucketIfNotExists(notifyCustomerBucketName);
    }

    private void createBucketIfNotExists(String bucketName) {
        BucketManager bucketManager = couchbaseCluster.buckets();

        BucketSettings bucketSettings = BucketSettings.create(bucketName)
                .ramQuotaMB(100) // Set the memory quota for the bucket
                .replicaIndexes(true) // Enable replica indexes
                .numReplicas(1); // Set the number of replicas

        try {
            bucketManager.createBucket(bucketSettings);
        } catch (BucketExistsException e) {
            // The bucket already exists, no action needed
        }
    }



    /*
    Creating Bean for CouchbaseTemplate for both couchbase bucket
    Last Paramter in SimpleCouchbaseClientFactory is Scope, which is null in this case.
    The scope value in a Spring Boot application typically refers to the lifecycle of a bean. There are five types of bean scopes supported in Spring:
    singleton, prototype, request, session, and global-session.  In the context of your CouchbaseConfig class, you haven't specified a scope for your beans,
    which means they are singleton by default. This means that Spring will create a single instance of each bean per Spring IoC container.
     */


    @Bean("inventoryTemplate")
    public CouchbaseTemplate inventoryTemplate() {
        CouchbaseClientFactory factory = new SimpleCouchbaseClientFactory(couchbaseCluster, inventoryBucketName, null);
        return new CouchbaseTemplate(factory, new MappingCouchbaseConverter(new CouchbaseMappingContext()));
    }

    @Bean("notifyCustomerTemplate")
    public CouchbaseTemplate notifyCustomerTemplate() {
        CouchbaseClientFactory factory = new SimpleCouchbaseClientFactory(couchbaseCluster, notifyCustomerBucketName, null);
        return new CouchbaseTemplate(factory, new MappingCouchbaseConverter(new CouchbaseMappingContext()));
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

}
