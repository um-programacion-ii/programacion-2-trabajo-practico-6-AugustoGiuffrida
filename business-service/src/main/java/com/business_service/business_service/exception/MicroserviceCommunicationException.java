package com.business_service.business_service.exception;

public class MicroserviceCommunicationException extends RuntimeException{
    public MicroserviceCommunicationException(){
        super("Error de comunicación con el servicio de datos");
    }
}
