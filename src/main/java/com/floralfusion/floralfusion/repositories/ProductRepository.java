package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.entities.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCompany(Company company);
    List<Product> findByCompanyCompanyID(Long companyID);
    Optional<Product> findById(Long id);
}
