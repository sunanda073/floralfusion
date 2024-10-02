package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
