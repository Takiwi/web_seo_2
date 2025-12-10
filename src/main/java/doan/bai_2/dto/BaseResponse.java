package doan.bai_2.dto;

import doan.bai_2.enums.HttpStatus;

public class BaseResponse <T>{
    private String message;
    private HttpStatus statusCode;
    private T data = null;

    public BaseResponse(){}

    public BaseResponse(String message){
        this.message = message;
    }

    public BaseResponse(String message, HttpStatus statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }

    public BaseResponse(String message, HttpStatus statusCode, T data){
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
