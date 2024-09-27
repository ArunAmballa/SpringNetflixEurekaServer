package com.eazybytes.accounts.services.Impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerAccountsResponseDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entities.Accounts;
import com.eazybytes.accounts.entities.Customer;
import com.eazybytes.accounts.exceptions.CustomerAlreadyExistException;
import com.eazybytes.accounts.exceptions.ResourceNotFoundException;
import com.eazybytes.accounts.repositories.AccountsRepository;
import com.eazybytes.accounts.repositories.CustomerRepository;
import com.eazybytes.accounts.services.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountsServiceImplementation implements IAccountsService {

    private final AccountsRepository accountsRepository;

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CustomerAccountsResponseDto createAccount(CustomerDto customerDto) {

        Optional<Customer> customer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(customer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already exist with given mobile number"+customerDto.getMobileNumber());
        }

        Customer customerToBeSaved = modelMapper.map(customerDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customerToBeSaved);

        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(savedCustomer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        Accounts savedAccount = accountsRepository.save(newAccount);

        AccountsDto accountsDto = modelMapper.map(savedAccount, AccountsDto.class);

        CustomerAccountsResponseDto customerAccountsResponseDto = CustomerAccountsResponseDto
                .builder()
                .name(savedCustomer.getName())
                .mobileNumber(savedCustomer.getMobileNumber())
                .email(savedCustomer.getEmail())
                .accountsDto(accountsDto)
                .build();

        return customerAccountsResponseDto;
    }

    @Override
    public CustomerAccountsResponseDto updateAccount(CustomerDto customerDto, String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer with mobile number " + mobileNumber + " not found"));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account for Specific Customer  not found"));

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        Customer savedCustomer = customerRepository.save(customer);

        AccountsDto accountsDto = modelMapper.map(accounts, AccountsDto.class);

        CustomerAccountsResponseDto customerAccountsResponseDto = CustomerAccountsResponseDto
                .builder()
                .name(savedCustomer.getName())
                .mobileNumber(savedCustomer.getMobileNumber())
                .email(savedCustomer.getEmail())
                .accountsDto(accountsDto)
                .build();

        return customerAccountsResponseDto;

    }

    @Override
    public CustomerAccountsResponseDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer with mobile number " + mobileNumber + " not found"));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account for Specific Customer not found"));

        AccountsDto accountsDto = modelMapper.map(accounts, AccountsDto.class);

        CustomerAccountsResponseDto customerAccountsResponseDto = CustomerAccountsResponseDto
                .builder()
                .name(customer.getName())
                .mobileNumber(customer.getMobileNumber())
                .email(customer.getEmail())
                .accountsDto(accountsDto)
                .build();
        return customerAccountsResponseDto;
    }

    @Override
    public Boolean deleteAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer with mobile number " + mobileNumber + " not found"));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account for Specific Customer not found"));

        accountsRepository.delete(accounts);
        customerRepository.delete(customer);

        return true;
    }
}
