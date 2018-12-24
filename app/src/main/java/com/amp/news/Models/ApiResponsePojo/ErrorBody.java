package com.amp.news.Models.ApiResponsePojo;

/**
 * Created by amal on 24/12/18.
 */

public class ErrorBody {
    private int cod;
    private String message;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
