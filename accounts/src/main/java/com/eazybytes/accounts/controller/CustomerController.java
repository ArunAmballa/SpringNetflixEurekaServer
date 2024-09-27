package com.eazybytes.accounts.controller;


import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestParam String mobileNumber){

        CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber);
        return new ResponseEntity<>(customerDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/fetchLoans")
    public ResponseEntity<LoansDto> fetchLoansDetails(@RequestParam String mobileNumber){
        LoansDto loansDetails = customerService.getLoansDetails(mobileNumber);
        return new ResponseEntity<>(loansDetails, HttpStatus.OK);
    }
}
