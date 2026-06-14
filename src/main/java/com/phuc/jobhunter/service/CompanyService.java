package com.phuc.jobhunter.service;

import com.phuc.jobhunter.domain.Company;
import com.phuc.jobhunter.domain.dto.Meta;
import com.phuc.jobhunter.domain.dto.ResultPaginationDTO;
import com.phuc.jobhunter.repository.CompanyRepository;
import com.phuc.jobhunter.util.error.IdInvalidException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService (CompanyRepository companyRepository){
        this.companyRepository=companyRepository;
    }

    public Company createCompany(Company company){
        return companyRepository.save(company);

    }

    public Company getCompanyById(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy company với ID: " + id));
    }


    public ResultPaginationDTO getAllCompany(Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageCompany.getNumber() +1);
        mt.setPageSize(pageCompany.getSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setResult(pageCompany.getContent());
        rs.setMeta(mt);


        return rs;

    }

    public Company updateCompany(Long id, Company company){
        Company company1 = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("khong tim thay user"));
        company1.setName(company.getName());
        company1.setAddress(company.getAddress());
        company1.setDescription(company.getDescription());
        company1.setLogo(company.getLogo());

        return companyRepository.save(company1);

    }


    public void deleteCompany( Long id){
        companyRepository.deleteById(id);
    }


}
