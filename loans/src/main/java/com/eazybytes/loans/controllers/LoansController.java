package com.eazybytes.loans.controllers;


import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.record.LoansInfoDto;
import com.eazybytes.loans.services.ILoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class LoansController {

    private final ILoansService loansService;

    private LoansInfoDto loansInfoDto;


    @PostMapping("/create")
    public ResponseEntity<LoansDto> createLoan(@RequestParam String mobileNumber) {
        LoansDto loan = loansService.createLoan(mobileNumber);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(@RequestParam String mobileNumber) {

        LoansDto loansDto = loansService.fetchLoan(mobileNumber);
        log.info(loansDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @PutMapping("/update")
    public ResponseEntity<LoansDto> updateLoan(@RequestBody LoansDto loansDto, @RequestParam String mobileNumber) {
        LoansDto updatedLoansDto = loansService.updateLoan(loansDto, mobileNumber);
        return new ResponseEntity<>(updatedLoansDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public Boolean deleteLoan(@RequestParam String mobileNumber) {

        boolean b = loansService.deleteLoan(mobileNumber);
        return b;
    }


    @GetMapping("/build-info")
    public ResponseEntity<LoansInfoDto> getLoansInfo() {
        return new ResponseEntity<>(loansInfoDto, HttpStatus.OK);
    }
}
