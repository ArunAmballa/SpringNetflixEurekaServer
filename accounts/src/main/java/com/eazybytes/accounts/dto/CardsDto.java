package com.eazybytes.accounts.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CardsDto {


    private String mobileNumber;

    private String cardNumber;


    private String cardType;


    private int totalLimit;


    private int amountUsed;

    private int availableAmount;

}
