package com.nacid.bl.esoed;

public class EsoedException extends Exception {
    public EsoedException(String message){
        super(message);
    }

    public EsoedException(String message, Throwable t){
        super(message,t);
    }

    public EsoedException(Throwable t){
        super(t);
    }

}