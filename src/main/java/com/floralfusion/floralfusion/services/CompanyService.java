package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.repositories.CompanyRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HttpSession session;

    public CompanyService(CompanyRepository companyRepository, HttpSession session) {
        this.companyRepository = companyRepository;
        this.session = session;
    }

    // Fetch company by ID
    public Optional<Company> findById(Long companyId) {
        return companyRepository.findById(companyId);
    }

    // Save a new company
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    // Check if a company exists by ID
    public boolean existsById(Long companyId) {
        return companyRepository.existsById(companyId);
    }

    public Optional<Company> getLoggedInUserCompany() {
        Long companyId = (Long) session.getAttribute("loggedInCompanyId");
        if (companyId == null) {
            return Optional.empty();
        }
        return companyRepository.findById(companyId);
    }
}
