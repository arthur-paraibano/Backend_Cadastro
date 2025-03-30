package com.duett.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.duett.api.model.domain.UserModel;

@Repository
@Service
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel existsByPassword(String password);

    boolean existsByName(String username);

    boolean existsByCpf(String cpf);

    UserDetails findByName(String username);

    UserDetails findByEmail(String username);
}
