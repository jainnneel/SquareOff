package com.squareoff.response;

import org.springframework.http.HttpStatus;

public class ResponseEntity {

    private String status;
    
    private String data;
    
    private HttpStatus httpStatus;

    public ResponseEntity() {
        super();
    }

    public ResponseEntity(String status, String data, HttpStatus httpStatus) {
        super();
        this.status = status;
        this.data = data;
        this.httpStatus = httpStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    
}
