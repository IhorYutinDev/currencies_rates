package ua.yutin.CurrencyRates.dtos;


import lombok.Data;

import java.util.Date;


@Data
public class ErrorResponse {
    private String error;
    private Date date;
    public ErrorResponse(String error) {
        this.error = error;
        this.date = new Date();
    }
}
