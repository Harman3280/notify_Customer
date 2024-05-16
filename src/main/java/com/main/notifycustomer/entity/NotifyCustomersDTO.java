package com.main.notifycustomer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NotifyCustomersDTO {

    @Id
    @NotNull
    private String id;

    @Field
    private List<String> customersData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCustomersData() {
        return customersData;
    }

    public void setCustomersData(List<String> customersData) {
        this.customersData = customersData;
    }
}
