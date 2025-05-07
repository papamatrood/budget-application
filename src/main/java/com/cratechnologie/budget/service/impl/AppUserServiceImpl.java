package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.AppUserService;
import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.repository.AppUserRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser save(AppUser appUser) {
        LOG.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser update(AppUser appUser) {
        LOG.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        LOG.debug("Request to partially update AppUser : {}", appUser);

        return appUserRepository
            .findById(appUser.getId())
            .map(existingAppUser -> {
                if (appUser.getAccountStatus() != null) {
                    existingAppUser.setAccountStatus(appUser.getAccountStatus());
                }
                if (appUser.getLastDateUpdate() != null) {
                    existingAppUser.setLastDateUpdate(appUser.getLastDateUpdate());
                }
                if (appUser.getDateCreated() != null) {
                    existingAppUser.setDateCreated(appUser.getDateCreated());
                }
                if (appUser.getFirstname() != null) {
                    existingAppUser.setFirstname(appUser.getFirstname());
                }
                if (appUser.getLastname() != null) {
                    existingAppUser.setLastname(appUser.getLastname());
                }
                if (appUser.getPhoneNumber() != null) {
                    existingAppUser.setPhoneNumber(appUser.getPhoneNumber());
                }
                if (appUser.getBirthDate() != null) {
                    existingAppUser.setBirthDate(appUser.getBirthDate());
                }
                if (appUser.getBirthPlace() != null) {
                    existingAppUser.setBirthPlace(appUser.getBirthPlace());
                }
                if (appUser.getGender() != null) {
                    existingAppUser.setGender(appUser.getGender());
                }
                if (appUser.getFamilySituation() != null) {
                    existingAppUser.setFamilySituation(appUser.getFamilySituation());
                }
                if (appUser.getPosition() != null) {
                    existingAppUser.setPosition(appUser.getPosition());
                }
                if (appUser.getAddress() != null) {
                    existingAppUser.setAddress(appUser.getAddress());
                }

                return existingAppUser;
            })
            .map(appUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        LOG.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
