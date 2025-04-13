package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUserUserID(Long userID);

    Optional<Admin> findByAdminID(Long adminId);

    Optional<Admin> findByUser_EmailAndUser_PasswordAndPasskey(String email, String password, String passkey);

}
