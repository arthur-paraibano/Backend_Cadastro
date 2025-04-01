package com.duett.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duett.api.controllers.dto.GeneralDto;
import com.duett.api.controllers.dto.UserDto;
import com.duett.api.controllers.dto.UserGeneralDto;
import com.duett.api.exception.ExistException;
import com.duett.api.exception.IncorrectParameterException;
import com.duett.api.exception.IsNullException;
import com.duett.api.model.domain.UserModel;
import com.duett.api.model.repository.UserRepository;
import com.duett.api.util.ValidationUtil;
import com.duett.api.util.internationalization.InternationalizationAjuste;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private InternationalizationAjuste messageIniernat;

    @Transactional
    public List<UserModel> findAll() {
        return repository.findAll();
    }

    @Transactional
    public UserModel findById(GeneralDto id) {
        if (id == null) {
            throw new IsNullException(messageIniernat.getMessage("id.null"));
        }
        Optional<UserModel> rs = repository.findById(id.Id());
        return rs.orElse(null);
    }

    @Transactional
    public UserModel delete(UserDto dto) {
        Optional<UserModel> chamado = repository.findById(dto.id());
        if (chamado.isPresent()) {
            repository.delete(chamado.get());
            return chamado.get();
        } else {
            throw new ExistException(messageIniernat.getMessage("not.found"));
        }
    }

    @Transactional
    public UserModel save(UserDto dto) {
        boolean isUpdate = !(dto.id() == null || dto.id() == 0);

        boolean nameExists = repository.existsByName(dto.name().toUpperCase());
        boolean emailExists = repository.findByEmail(dto.email().toUpperCase()) != null;
        boolean cpfExists = repository.existsByCpf(dto.cpf());

        if (dto.name().isEmpty() || dto.name().isBlank() || dto.email().isEmpty()
                || dto.email().isBlank() || dto.password().isBlank() || dto.password().isEmpty()
                || dto.profile().isEmpty() || dto.profile().isBlank()) {
            throw new IsNullException(messageIniernat.getMessage("descriptions.required"));
        }

        if (!ValidationUtil.isValidEmail(dto.email())) {
            throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.invalid.email"));
        }
        if (!ValidationUtil.isValidCpf(dto.cpf())) {
            throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.invalid.cpf"));
        }

        if (!isUpdate) {
            if (nameExists) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.name.exist"));
            }
            if (emailExists) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.email.exist"));
            }
            if (cpfExists) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.cpf.exist"));
            }
            if (!(dto.profile().toUpperCase().equals("ADMINISTRADOR")
                    || dto.profile().toUpperCase().equals("USUÁRIO"))) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descripcions.valid.profile"));
            }
        }

        if (isUpdate && !repository.findById(dto.id()).isPresent()) {
            throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.not.exist"));
        }

        String profileUpper = dto.profile().toUpperCase();
        if (!profileUpper.equals("ADMINISTRADOR") && !profileUpper.equals("USUÁRIO")) {
            throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.valid.profile"));
        }

        if (isUpdate) {
            UserModel existingByName = (UserModel) repository.findByName(dto.name().toUpperCase());
            UserDetails existingByEmail = repository.findByEmail(dto.email().toUpperCase());

            if (existingByName != null && !existingByName.getId().equals(dto.id())) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.name.exist"));
            }
            if (existingByEmail != null && !((UserModel) existingByEmail).getId().equals(dto.id())) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.email.exist"));
            }
            UserModel existingByCpf = repository.findAll().stream()
                    .filter(u -> u.getCpf().equals(dto.cpf()))
                    .findFirst()
                    .orElse(null);
            if (existingByCpf != null && !existingByCpf.getId().equals(dto.id())) {
                throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.user.cpf.exist"));
            }
        }

        UserModel user = repository.findById(dto.id()).orElse(null);
        UserModel userModel = new UserModel();
        userModel.setId(isUpdate ? dto.id() : null);
        userModel.setName(dto.name().toUpperCase());
        userModel.setEmail(dto.email().toUpperCase());
        userModel.setPassword(
                isUpdate && user != null ? user.getPassword() : new BCryptPasswordEncoder().encode(dto.password()));
        userModel.setCpf(dto.cpf());
        userModel.setProfile(profileUpper);
        userModel.setFirstLogin(false);

        return repository.save(userModel);
    }

    @Transactional
    public UserModel updatePassword(UserGeneralDto dto) {
        Optional<UserModel> userOpt = repository.findById(dto.id());
        if (userOpt.isEmpty()) {
            throw new IncorrectParameterException(messageIniernat.getMessage("descriptions.extraviado"));
        }
        if (dto.password() == null) {
            throw new IncorrectParameterException(messageIniernat.getMessage("login.is.username.password"));
        }
        UserModel user = userOpt.get();
        user.setPassword(new BCryptPasswordEncoder().encode(dto.password()));
        user.setFirstLogin(false);
        return repository.save(user);
    }
}
