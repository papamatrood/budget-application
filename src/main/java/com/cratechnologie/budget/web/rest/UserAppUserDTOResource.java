package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.AppUserService;
import com.cratechnologie.budget.service.MailService;
import com.cratechnologie.budget.service.UserAppUserDTOService;
import com.cratechnologie.budget.service.UserService;
import com.cratechnologie.budget.service.dto.AdminUserDTO;
import com.cratechnologie.budget.service.dto.UserAppUserDTO;
import com.cratechnologie.budget.config.Constants;
import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.domain.Authority;
import com.cratechnologie.budget.domain.User;
import com.cratechnologie.budget.repository.AppUserRepository;
import com.cratechnologie.budget.repository.UserRepository;
import com.cratechnologie.budget.security.AuthoritiesConstants;
import com.cratechnologie.budget.web.rest.errors.BadRequestAlertException;
import com.cratechnologie.budget.web.rest.errors.EmailAlreadyUsedException;
import com.cratechnologie.budget.web.rest.errors.LoginAlreadyUsedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/user-app-user")
public class UserAppUserDTOResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAppUserDTOResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "appUser";

    private final UserAppUserDTOService userAppUserDTOService;

    private final UserRepository userRepository;

    private final AppUserRepository appUserRepository;

    private final AppUserService appUserService;

    private final UserService userService;

    private final MailService mailService;

    public UserAppUserDTOResource(UserAppUserDTOService userAppUserDTOService, UserRepository userRepository,
            MailService mailService, AppUserService appUserService, UserService userService,
            AppUserRepository appUserRepository) {
        this.userAppUserDTOService = userAppUserDTOService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.appUserService = appUserService;
        this.userService = userService;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AppUser> createUser(@Valid @RequestBody UserAppUserDTO userAppUserDTO)
            throws URISyntaxException {
        LOG.debug("REST request to save User : {}", userAppUserDTO);

        if (userAppUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userAppUserDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userAppUserDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            AppUser newUser = userAppUserDTOService.createUserWithAppUser(userAppUserDTO);
            mailService.sendCreationEmail(newUser.getUser());
            return ResponseEntity.created(new URI("/api/create-user-app-user/users/" + newUser.getUser().getLogin()))
                    .headers(HeaderUtil.createAlert(applicationName, "userManagement.created",
                            newUser.getUser().getLogin()))
                    .body(newUser);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody UserAppUserDTO userAppUserDTO)
            throws URISyntaxException {
        LOG.debug("REST request to update AppUser : {}, {}", id, userAppUserDTO);
        if (userAppUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAppUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppUser appUser = userAppUserDTOService.updateAppUser(userAppUserDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        appUser.getId().toString()))
                .body(appUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable("id") Long id, @RequestBody UserAppUserDTO userAppUserDTO) {
        LOG.debug("REST request to delete AppUser : {}", userAppUserDTO.getId());
        userAppUserDTOService.deleteAppUser(userAppUserDTO);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, userAppUserDTO.getId().toString()))
                .build();
    }

}
