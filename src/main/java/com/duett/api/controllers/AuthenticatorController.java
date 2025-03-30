package com.duett.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.duett.api.controllers.dto.AuthenticatorDto;
import com.duett.api.controllers.dto.LoginResponseDto;
import com.duett.api.controllers.dto.UserDto;
import com.duett.api.exception.ExistException;
import com.duett.api.exception.InternalServer;
import com.duett.api.exception.InvalidInput;
import com.duett.api.exception.IsNullException;
import com.duett.api.infra.exception.RestMessage;
import com.duett.api.infra.security.TokenService;
import com.duett.api.model.domain.UserModel;
import com.duett.api.model.repository.UserRepository;
import com.duett.api.service.UserService;
import com.duett.api.util.internationalization.InternationalizationAjuste;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = { "application/json" })
@Tag(name = "Authenticator API", description = "authenticator Controller")
public class AuthenticatorController {

    @Autowired
    private InternationalizationAjuste messageIniernat;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticatorDto dto) {
        try {
            if (dto.name() == null || dto.name().isBlank() || dto.password() == null || dto.password().isBlank()) {
                throw new IsNullException(messageIniernat.getMessage("login.is.required"));
            }
            UserDetails userOpt = userRepository.findByName(dto.name().toUpperCase());
            if (userOpt == null) {
                throw new ExistException(messageIniernat.getMessage("login.is.username"));
            }

            UsernamePasswordAuthenticationToken uAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    dto.name().toUpperCase(), dto.password());
            Authentication authentication = authenticationManager.authenticate(uAuthenticationToken);
            UserModel user = (UserModel) authentication.getPrincipal();
            String token = tokenService.generateToken(user);
            LoginResponseDto response = new LoginResponseDto(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getProfile(),
                    user.isFirstLogin());
            return ResponseEntity.ok(response);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (BadRequest e) {
            throw new InvalidInput(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto dto) {
        try {
            boolean isUpdate = !(dto.id() == null || dto.id() == 0);
            RestMessage response = null;
            if (isUpdate) {
                throw new InternalServer(messageIniernat.getMessage("controller.restmenssage.error.create"));
            }
            service.save(dto);
            if (!isUpdate) {
                response = new RestMessage(201, messageIniernat.getMessage("controller.restmenssage.success.create"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (BadRequest e) {
            throw new InvalidInput(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }
}
