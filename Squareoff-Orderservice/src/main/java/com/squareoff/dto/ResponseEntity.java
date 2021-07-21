package com.squareoff.dto;

import org.springframework.http.HttpStatus;

public class ResponseEntity {

    private Object data;
    
    private String status;
    
    private HttpStatus httpStatus;

    public ResponseEntity() {
        super();
    }

    public ResponseEntity(Object data, String status, HttpStatus httpStatus) {
        super();
        this.data = data;
        this.status = status;
        this.httpStatus = httpStatus;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "ResponseEntity [data=" + data + ", status=" + status + ", httpStatus=" + httpStatus + "]";
    }
    
    
}
