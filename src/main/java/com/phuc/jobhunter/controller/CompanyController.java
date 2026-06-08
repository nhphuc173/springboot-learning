package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.Company;
import com.phuc.jobhunter.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController (CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody @Valid Company company){


        return ResponseEntity.ok(companyService.createCompany(company));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getCompanyById(id));
    }

    @GetMapping("/all-company")
    public ResponseEntity<List<Company>> getAllCompany(){
        List<Company> listCompany = this.companyService.getAllCompany();
        return ResponseEntity.status(HttpStatus.OK).body(listCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company, @PathVariable Long id){
        return ResponseEntity.ok(this.companyService.updateCompany(id, company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaCompany (@PathVariable Long id){
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
