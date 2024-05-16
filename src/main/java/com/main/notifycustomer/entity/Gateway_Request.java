package com.main.notifycustomer.entity;

/*
{
  userid: ""
  product_id: ""
  operation: "add/remove"
}

@Document: Identifies a domain object to be persisted to Couchbase.
@Id: Identifies a field which will not be stored in the document but rather used as the document ID
@Field: Denotes a field in the Couchbase document.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@Document
public class Gateway_Request {

    @Id
    @NotNull
    final String id;

   @Field
   @NotNull
   String userid;

   @Field
   @NotNull
   String product_id;

   @Field
   @NotNull
   String operation;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
