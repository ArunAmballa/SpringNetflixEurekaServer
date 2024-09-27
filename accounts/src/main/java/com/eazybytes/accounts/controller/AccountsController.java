package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.CustomerAccountsResponseDto;
import com.eazybytes.accounts.record.AccountsInfoDto;
import com.eazybytes.accounts.services.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class AccountsController {

    private final IAccountsService accountsService;

    @Autowired
    private AccountsInfoDto accountsInfoDto;


    @PostMapping("/create")
    public ResponseEntity<CustomerAccountsResponseDto> createAccount(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(accountsService.createAccount(customerDto), HttpStatus.CREATED);
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerAccountsResponseDto> fetchAccount(@RequestParam  String mobileNumber){
        CustomerAccountsResponseDto customerAccountsResponseDto = accountsService.fetchAccount(mobileNumber);
        return new ResponseEntity<>(customerAccountsResponseDto, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<CustomerAccountsResponseDto> updateAccount(@RequestParam String mobileNumber, @RequestBody CustomerDto customerDto){

        CustomerAccountsResponseDto customerResponseDto = accountsService.updateAccount(customerDto,mobileNumber);

        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public Boolean deleteAccount(@RequestParam String mobileNumber){
        Boolean b = accountsService.deleteAccount(mobileNumber);
        return b;

    }

    @GetMapping("/build-info")
    public ResponseEntity<AccountsInfoDto> getAccountsInfo(){
        return new ResponseEntity<>(accountsInfoDto,HttpStatus.OK);
    }



}
