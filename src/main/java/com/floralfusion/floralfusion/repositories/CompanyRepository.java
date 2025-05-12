package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByUser_UserID(Long userID);

    Company findByUser(User user);
}
