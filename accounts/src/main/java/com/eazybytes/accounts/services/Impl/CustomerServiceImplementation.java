package com.eazybytes.accounts.services.Impl;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.entities.Accounts;
import com.eazybytes.accounts.entities.Customer;
import com.eazybytes.accounts.exceptions.ResourceNotFoundException;
import com.eazybytes.accounts.repositories.AccountsRepository;
import com.eazybytes.accounts.repositories.CustomerRepository;
import com.eazybytes.accounts.services.ICustomerService;
import com.eazybytes.accounts.services.client.CardsFeignClient;
import com.eazybytes.accounts.services.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImplementation implements ICustomerService {

    private  CustomerRepository customerRepository;

    private  AccountsRepository accountsRepository;

    private  LoansFeignClient loansFeignClient;

    private  CardsFeignClient cardsFeignClient;

    private final ModelMapper modelMapper;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer with given mobile Number Not Found")
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account for Given Customer NNot found")
        );

        AccountsDto accountsDto = modelMapper.map(accounts, AccountsDto.class);

        CustomerDetailsDto customerDetailsDto = new CustomerDetailsDto();
        customerDetailsDto.setMobileNumber(customer.getMobileNumber());
        customerDetailsDto.setName(customer.getName());
        customerDetailsDto.setEmail(customer.getEmail());
        customerDetailsDto.setAccountsDto(accountsDto);

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoan(mobileNumber);
        log.info(loansDtoResponseEntity.getBody().toString());
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCard(mobileNumber);
        log.info(cardsDtoResponseEntity.getBody().toString());
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }

    public LoansDto getLoansDetails(String mobileNumber) {
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoan(mobileNumber);
        log.info(loansDtoResponseEntity.getBody().toString());
        return loansDtoResponseEntity.getBody();
    }
}
