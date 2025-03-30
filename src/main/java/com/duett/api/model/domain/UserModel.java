package com.duett.api.model.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class UserModel extends RepresentationModel<UserModel> implements Serializable, UserDetails {

        @Serial
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_tb_users")
        private Integer id;

        @Column(name = "tb_users_username", nullable = false)
        private String name;

        @Column(name = "tb_users_email", nullable = false)
        private String email;

        @JsonIgnore
        @Column(name = "tb_users_password", nullable = false)
        private String password;

        @Column(name = "tb_users_cpf", nullable = false)
        private String cpf;

        @Column(name = "tb_users_firstLogin", nullable = false)
        private boolean firstLogin;

        @Column(name = "tb_users_profile", nullable = false)
        private String profile;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                if (this.profile.equals("ADMINISTRADOR")) {
                        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_US"));
                } else {
                        return List.of(new SimpleGrantedAuthority("ROLE_US"));
                }
        }

        @Override
        public String getUsername() {
                return this.name;
        }
}
