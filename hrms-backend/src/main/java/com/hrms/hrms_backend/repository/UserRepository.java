package com.hrms.hrms_backend.repository;
import com.hrms.hrms_backend.constants.Role;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> getUsersByRoleEquals(Role role);
}
