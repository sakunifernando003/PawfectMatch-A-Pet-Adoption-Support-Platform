package org.example.back_end.repo;

import org.example.back_end.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByRoleName(String roleName);
}
