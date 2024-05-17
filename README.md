# Notify Customer
The "Notify Customer" feature is a consolidated system that tracks products and customers who have expressed interest in being notified about those products. When a customer clicks the "Notify Me" button for a product that is currently unavailable, their request is logged in the system. Once the product becomes available again, the system automatically sends a notification to all customers who had requested to be notified. This feature ensures that customers are promptly informed about the availability of their desired products, enhancing the customer experience and potentially boosting sales.


The **"addToInventory" POST** request is used to add a product to the inventory. Upon this action, a message is produced in a Kafka topic. A consumer then consumes this message, retrieves a list of customers associated with the product ID from the "Notify_customer" Couchbase bucket, and sends a notification to those customers who had previously selected the "Notify Me" option.  

The **"notifyMe/{productId}"** POST request is triggered when a customer clicks on the "Notify Me" option. This request adds the customer's email address to the "notify_Customer" Couchbase bucket. This way, when the product becomes available, the system can notify the customer.
<br/>
<br/>
<br/>
<br/>
<br/>
Below are some explanain for the how to publish String and JSON type messeages in kafka topic

Spring boot kafka application with multiple Producers and multiple Consumers for String data and JSON object - 
This project explains How to **publish** message in kafka Topic 
and **consume** a message from Kafka Topic. Here message is in String and Json Object format.  
In this application there are two publishers, i.e. one for String data and another one is for publishing object. 
For those two publishers, two `KafkaTemplate`s are used.  
To consume those messages and objects two consumers are used. Two `@KafkaListner`s are used to consume respective data.

## Prerequisites 
- Java
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Kafka](https://kafka.apache.org/documentation/)
- [Couchbase](https://docs.couchbase.com/couchbase-lite/current/c/gs-downloads.html)

## Tools
- Eclipse or IntelliJ IDEA (or any preferred IDE) with embedded Gradle
- Postman (or any RESTful API testing tool)
- Couchbase
- Condukta Kafka

<br/>

##### Couchbase
Download Couchbase which is NoSQL database. 
After download , just need to start with local host and port number.

##### Conduktor
Download Conduktor Tool which having Zookepeer and kafka inbuilt. 
After download , just need to start with local host and port number.


### Code Snippets
1. #### Maven Dependencies
    Need to add below dependency to enable kafka in **pom.xml**.  
    ```
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-couchbase</artifactId>
    </dependency>
    ```
   
2. #### Properties file
     Reading some properties from **application.yml** file, like bootstrap servers, group id and topics.  
     Here we have two topics to publish and consume data.
     > message-topic (for string data)  
       superhero-topic (for SuperHero objects)

     **src/main/resources/application.yml**
     ```
     spring:
       kafka:
         consumer:
           bootstrap-servers: localhost:9092
           group-id: group_id
   
         producer:
           bootstrap-servers: localhost:9092
   
         topic: message-topic
         superhero-topic: superhero-topic  
     ```

     application.yml file used in Project

   ```
      values:
        couchbase:
          host: "localhost"
          username: "Administrator"
          password: "Administrator"
          Buckets:
            inventory:
              bucketName: "inventory"
              keyLength: 10
            notify_Customer:
              bucketName: "notify_Customer"
      spring:
        kafka:
          consumer:
            bootstrap-servers: localhost:9092
            group-id: notifyConsumer
            key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
          properties:
            spring.deserializer.value.delegate.class: org.springframework.kafka.support.serializer.JsonDeserializer
        #        auto-offset-reset: earliest
        #        key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        #        value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        producer:
          bootstrap-servers: localhost:9092
          retry:
            maxAttempts: 3
        #        key-serializer: org.apache.kafka.common.serialization.StringSerializer
        #        value-serializer: org.apache.kafka.common.serialization.StringSerializer
        topic: notify_Customer
      mail:
        host: "smtp.gmail.com"
        port: "587"
        username: "yourEmail@gmail.com"
        password: "--"
        properties:
          mail.smtp.auth: true
          mail.smtp.starttls.enable: true

   ```
   
4. #### Model class
    This is the model class which we will publish kafka topic using `KafkaTemplate` and consume it using `@KafkaListner` from the same topic.  
    **com.arya.kafka.model.SuperHero.java**  
    ```
    public class SuperHero implements Serializable {
    
        private String name;
        private String superName;
        private String profession;
        private int age;
        private boolean canFly;
   
        // Constructor, Getter and Setter
    }
    ```

5. #### Kafka Configuration
    The kafka producer related configuration is under **com.arya.kafka.config.KafkaProducerConfig.java** class.  
    This class is marked with `@Configuration` annotation. For JSON producer we have to set value serializer property to `JsonSerializer.class`
    and have to pass that factory to KafkaTemplate.  
    For String producer we have to set value serializer property to `StringSerializer.class` and have to pass that factory to new KafkaTemplate. 
    - Json Producer configuration
      ```
      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      ```
      ```
      @Bean
      public <T> KafkaTemplate<String, T> kafkaTemplate() {
          return new KafkaTemplate<>(producerFactory());
      }  
      ``` 
      
    - String Producer configuration
      ```
      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      ```
      ```
      @Bean
      public KafkaTemplate<String, String> kafkaStringTemplate() {
          return new KafkaTemplate<>(producerStringFactory());
      }
      ```
          
    The kafka consumer related configuration is under **com.arya.kafka.config.KafkaConsumerConfig.java** class.  
    This class is marked with `@Configuration` and `@EnableKafka` (mandatory to consume the message in config class or main class) annotation. 
    For JSON consumer we have to set value deserializer property to `JsonDeserializer.class` and have to pass that factory to ConsumerFactory.  
    For String consumer we have to set value deserializer property to `StringDeserializer.class` and have to pass that factory to new ConsumerFactory.
    - Json Consumer configuration
      ```
      @Bean
      public ConsumerFactory<String, SuperHero> consumerFactory() {
         Map<String, Object> config = new HashMap<>();
     
         config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
         config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
         config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
         config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
         config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
     
         return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(SuperHero.class));
      }
      
      @Bean
      public <T> ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerJsonFactory() {
         ConcurrentKafkaListenerContainerFactory<String, SuperHero> factory = new ConcurrentKafkaListenerContainerFactory<>();
         factory.setConsumerFactory(consumerFactory());
         factory.setMessageConverter(new StringJsonMessageConverter());
         factory.setBatchListener(true);
         return factory;
      }     
   
    - String Consumer configuration
      ``` 
      @Bean
      public ConsumerFactory<String, String> stringConsumerFactory() {
         Map<String, Object> config = new HashMap<>();
   
         config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
         config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
         config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
         config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
         config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
   
         return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
      }
   
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringFactory() {
         ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
         factory.setConsumerFactory(stringConsumerFactory());
         factory.setBatchListener(true);
         return factory;
      }
      
6. #### Publishing data to Kafka Topic
    In **com.arya.kafka.service.ProducerService.java** class both String and JSON `KafkaTemplate`s are autowired 
    and using send() method we can publish data to kafka topics.  
    - Publishing Json Object
        ```
        @Autowired
        private KafkaTemplate<String, T> kafkaTemplateSuperHero;
       
        public void sendSuperHeroMessage(T superHero) {
            logger.info("#### -> Publishing SuperHero :: {}", superHero);
            kafkaTemplateSuperHero.send(superHeroTopic, superHero);
        }
        ```
    - Publishing String message
        ```
        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;
        
        public void sendMessage(String message) {
            logger.info("#### -> Publishing message -> {}", message);
            kafkaTemplate.send(topic, message);
        }
        ```
      
7. #### Consuming data from Kafka Topic
    In **com.arya.kafka.service.ConsumerService.java** class, we are consuming data from topics using `@KafkaListener` annotation.
    We are binding consumer factory from **KafkaConsumerConfig.java** class to **containerFactory** in KafkaListener.  
    ```
    // String Consumer
    @KafkaListener(topics = {"${spring.kafka.topic}"}, containerFactory = "kafkaListenerStringFactory", groupId = "group_id")
    public void consumeMessage(String message) {
        logger.info("**** -> Consumed message -> {}", message);
    }        
    
    // Object Consumer   
    @KafkaListener(topics = {"${spring.kafka.superhero-topic}"}, containerFactory = "kafkaListenerJsonFactory", groupId = "group_id")
    public void consumeSuperHero(SuperHero superHero) {
        logger.info("**** -> Consumed Super Hero :: {}", superHero);
    }
    ```
   
### API Endpoints

   
> **POST Mapping** http://localhost:8080/productInventory/v1/addToInventory
                                                    
  Request Body  
  ```
    {
      "price": "80000",
      "title": "Samsung",
      "specification": "128GB RAM , White Color",
      "quantity": "200",
      "productType": {
          "productType": "Electronics",
          "productCategory": "Mobile"
      },
      "tags": ["Electronics", "Mobile", "Smartphone", "Android"],
      "isActive": true
    }
  ```

  > **POST Mapping** http://localhost:8080/productInventory/v1/notifyMe/{productId}
                                                    
  Request Body  
  ```
    {
      "customerId": "yogesh@gmail.com"
    }
  ```


