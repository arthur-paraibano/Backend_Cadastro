package com.duett.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duett.api.controllers.dto.GeneralDto;
import com.duett.api.controllers.dto.UserDto;
import com.duett.api.controllers.dto.UserGeneralDto;
import com.duett.api.exception.InternalServer;
import com.duett.api.infra.exception.RestMessage;
import com.duett.api.model.domain.UserModel;
import com.duett.api.service.UserService;
import com.duett.api.util.internationalization.InternationalizationAjuste;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user", produces = { "application/json" })
@Tag(name = "User API", description = "User Controller")
public class UserController {

    @Autowired
    public UserService service;

    @Autowired
    private InternationalizationAjuste messageIniernat;

    @GetMapping("/all")
    @Operation(summary = "Find all Users", description = "Find all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserModel>> findAll() {
        List<UserModel> userModels = service.findAll();

        for (UserModel uModel : userModels) {
            Integer id = uModel.getId();
            GeneralDto dtoId = new GeneralDto(id);
            uModel.add(linkTo(methodOn(UserController.class).getUserById(dtoId)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModels);

    }

    @PostMapping("/id")
    @Operation(summary = "Find user by id", description = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "204", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserModel> getUserById(@Valid @RequestBody GeneralDto id) {
        UserModel uModel = service.findById(id);
        if (uModel != null) {
            uModel.add(linkTo(methodOn(UserController.class).findAll()).withRel("findAll"));
            return ResponseEntity.status(HttpStatus.OK).body(uModel);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/update")
    @Operation(summary = "Update user content update", description = "Update user content update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> updateType(@Validated @RequestBody UserDto dto) {
        boolean isUpdate = !(dto.id() == null || dto.id() == 0);
        RestMessage response = null;
        if (!isUpdate) {
            throw new InternalServer(messageIniernat.getMessage("controller.restmenssage.error.update",
                    linkTo(methodOn(AuthenticatorController.class).create(dto)).toString()));
        }
        UserModel update = service.save(dto);
        GeneralDto dtoId = new GeneralDto(update.getId());
        update.add(linkTo(methodOn(UserController.class).getUserById(dtoId)).withSelfRel());
        if (isUpdate) {
            response = new RestMessage(201, messageIniernat.getMessage("controller.restmenssage.success.update"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update-password")
    @Operation(summary = "User change password", description = "User change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> updatePassword(@Validated @RequestBody UserGeneralDto dto) {
        UserModel reset = service.updatePassword(dto);
        GeneralDto dtoId = new GeneralDto(reset.getId());
        reset.add(linkTo(methodOn(UserController.class).getUserById(dtoId)).withSelfRel());
        RestMessage response = new RestMessage(200,
                messageIniernat.getMessage("controller.restmenssage.success.updatePassword"));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "User change password", description = "User change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> delete(@Validated @RequestBody UserDto dto) {
        service.delete(dto);
        RestMessage response = new RestMessage(200,
                messageIniernat.getMessage("controller.restmenssage.success.delete"));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
