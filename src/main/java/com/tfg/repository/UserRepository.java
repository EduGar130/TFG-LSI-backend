package com.tfg.repository;

import com.tfg.entity.Role;
import com.tfg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByValido(boolean b);

    @Modifying
    @Query(value = "UPDATE users SET valido = false WHERE id = :id", nativeQuery = true)
    void setUserInvalidoById(@Param("id") Long id);
}
