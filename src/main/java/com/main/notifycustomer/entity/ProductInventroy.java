package com.main.notifycustomer.entity;

/*
entity class representing JSON document which we want to persist
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

@Document: Identifies a domain object to be persisted to Couchbase.
@Id: Identifies a field which will not be stored in the document but rather used as the document ID
@Field: Denotes a field in the Couchbase document.
 */

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Document
public class ProductInventroy {

    @Id
    @NotNull
    private String id;

    @Field
    private String update_time;

    @Field
    private String price;

    @Field
    private String title;

    @Field
    private String quantity;

    @Field
    private ProductType productType;

    @Field
    private String specification;

    @Field
    private List<String> tags;

    @Field
    private boolean isActive;

    public ProductInventroy() {
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
