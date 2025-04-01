package com.duett.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.duett.api.model.domain.UserModel;
import com.duett.api.model.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            // Usuário Administrador
            UserModel admin = new UserModel();
            admin.setName("ADMIN");
            admin.setEmail("admin@duett.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            admin.setCpf("123.456.789-00");
            admin.setProfile("ADMINISTRADOR");
            userRepository.save(admin);

            // Usuário Comum
            UserModel user = new UserModel();
            user.setName("JOAO");
            user.setEmail("joao@duett.com");
            user.setPassword(new BCryptPasswordEncoder().encode("joao123"));
            user.setCpf("987.654.321-00");
            user.setProfile("USUÁRIO");
            userRepository.save(user);
        }
    }
}