package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.Company;
import com.phuc.jobhunter.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController (CompanyService companyService){
        this.companyService = companyService;
    }

    public ResponseEntity<Company> createCompany(@RequestBody @Valid Company company){


        return ResponseEntity.ok(companyService.createCompany(company));

    }

}
