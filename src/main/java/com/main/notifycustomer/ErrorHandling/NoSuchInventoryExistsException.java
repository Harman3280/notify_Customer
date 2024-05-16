package com.main.notifycustomer.ErrorHandling;

public class NoSuchInventoryExistsException extends RuntimeException{

    private String message;

    public NoSuchInventoryExistsException() {}

    public NoSuchInventoryExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
