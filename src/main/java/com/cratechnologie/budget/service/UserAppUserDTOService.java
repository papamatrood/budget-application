package com.cratechnologie.budget.service;

import com.cratechnologie.budget.service.dto.AdminUserDTO;
import com.cratechnologie.budget.service.dto.UserAppUserDTO;
import com.cratechnologie.budget.service.dto.UserDTO;
import com.cratechnologie.budget.service.impl.AppUserServiceImpl;

import jakarta.persistence.EntityNotFoundException;

import com.cratechnologie.budget.config.Constants;
import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.domain.Authority;
import com.cratechnologie.budget.domain.User;
import com.cratechnologie.budget.repository.AppUserRepository;
import com.cratechnologie.budget.repository.AuthorityRepository;
import com.cratechnologie.budget.repository.UserRepository;
import com.cratechnologie.budget.security.AuthoritiesConstants;
import com.cratechnologie.budget.security.SecurityUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

@Service
@Transactional
public class UserAppUserDTOService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final Logger LOGAppUser = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserAppUserDTOService(
            UserRepository userRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository,
            CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public AppUser createUserWithAppUser(UserAppUserDTO userAppUserDTO) {
        // Create Jhipster User
        User createdJhipsterUser = this.createJhipsterUser(userAppUserDTO);

        // Create AppUser
        AppUser appUserDTO = new AppUser();
        appUserDTO.firstname(userAppUserDTO.getFirstName());
        appUserDTO.lastname(userAppUserDTO.getLastName());
        appUserDTO.setAccountStatus(true);
        appUserDTO.setPhoneNumber(userAppUserDTO.getPhoneNumber());
        appUserDTO.setBirthDate(userAppUserDTO.getBirthDate());
        appUserDTO.setBirthPlace(userAppUserDTO.getBirthPlace());
        appUserDTO.setGender(userAppUserDTO.getGender());
        appUserDTO.setFamilySituation(userAppUserDTO.getFamilySituation());
        appUserDTO.setPosition(userAppUserDTO.getPosition());
        appUserDTO.setAddress(userAppUserDTO.getAddress());
        appUserDTO.setUser(createdJhipsterUser);

        return appUserRepository.save(appUserDTO);

        // return createdJhipsterUser;
    }

    public AppUser updateAppUser(UserAppUserDTO userAppUserDTO) {
        User user = this.updateJhipsterUser(userAppUserDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        AppUser appUser = appUserRepository.findById(userAppUserDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found"));

        appUser.setPhoneNumber(userAppUserDTO.getPhoneNumber());
        appUser.setBirthDate(userAppUserDTO.getBirthDate());
        appUser.setBirthPlace(userAppUserDTO.getBirthPlace());
        appUser.setGender(userAppUserDTO.getGender());
        appUser.setFamilySituation(userAppUserDTO.getFamilySituation());
        appUser.setPosition(userAppUserDTO.getPosition());
        appUser.setAddress(userAppUserDTO.getAddress());
        appUser.setAccountStatus(userAppUserDTO.getAccountStatus());
        appUser.setUser(user);
        appUser.setFirstname(userAppUserDTO.getFirstName());
        appUser.setLastname(userAppUserDTO.getLastName());

        LOG.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    public void deleteAppUser(UserAppUserDTO userAppUserDTO) {
        if (userAppUserDTO.getLogin() != null) {            
            this.deleteJhipsterUser(userAppUserDTO.getLogin());
        }
        LOG.debug("Request to delete AppUser : {}", userAppUserDTO.getId());
        appUserRepository.deleteById(userAppUserDTO.getId());
    }

    private void deleteJhipsterUser(String login) {
        userRepository
                .findOneByLogin(login)
                .ifPresent(user -> {
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                    LOG.debug("Deleted User: {}", user);
                });
    }

    private Optional<User> updateJhipsterUser(UserAppUserDTO userAppUserDTO) {
        if (userAppUserDTO == null || userAppUserDTO.getJhipsterUserId() == null) {
            return Optional.empty();
        }

        return userRepository.findById(userAppUserDTO.getJhipsterUserId())
                .map(user -> {
                    this.clearUserCaches(user);

                    // Mise à jour des champs de base (en vérifiant null)
                    if (userAppUserDTO.getLogin() != null) {
                        user.setLogin(userAppUserDTO.getLogin().toLowerCase());
                    }
                    if (userAppUserDTO.getFirstName() != null) {
                        user.setFirstName(userAppUserDTO.getFirstName());
                    }
                    if (userAppUserDTO.getLastName() != null) {
                        user.setLastName(userAppUserDTO.getLastName());
                    }
                    if (userAppUserDTO.getEmail() != null) {
                        user.setEmail(userAppUserDTO.getEmail().toLowerCase());
                    }
                    if (userAppUserDTO.getImageUrl() != null) {
                        user.setImageUrl(userAppUserDTO.getImageUrl());
                    }
                    if (userAppUserDTO.getLangKey() != null) {
                        user.setLangKey(userAppUserDTO.getLangKey());
                    }

                    // Gestion de l'activation
                    user.setActivated(userAppUserDTO.isActivated());

                    // Mise à jour des rôles (si présents dans le DTO)
                    if (userAppUserDTO.getAuthorities() != null) {
                        Set<Authority> managedAuthorities = user.getAuthorities();
                        managedAuthorities.clear();
                        userAppUserDTO.getAuthorities().stream()
                                .map(authorityRepository::findById)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .forEach(managedAuthorities::add);
                    }

                    userRepository.save(user);
                    this.clearUserCaches(user);
                    LOG.debug("Changed Information for User: {}", user);
                    return user;
                });
    }

    private User createJhipsterUser(UserAppUserDTO userAppUserDTO) {
        User user = new User();
        user.setLogin(userAppUserDTO.getLogin().toLowerCase());
        user.setFirstName(userAppUserDTO.getFirstName());
        user.setLastName(userAppUserDTO.getLastName());
        if (userAppUserDTO.getEmail() != null) {
            user.setEmail(userAppUserDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userAppUserDTO.getImageUrl());
        if (userAppUserDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userAppUserDTO.getLangKey());
        }
        // String encryptedPassword =
        // passwordEncoder.encode(RandomUtil.generatePassword());
        String encryptedPassword = passwordEncoder.encode(userAppUserDTO.getLogin().toLowerCase() + "123");
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userAppUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = userAppUserDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        LOG.debug("Created Information for User: {}", user);
        return user;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE))
                .evictIfPresent(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE))
                    .evictIfPresent(user.getEmail());
        }
    }

}
