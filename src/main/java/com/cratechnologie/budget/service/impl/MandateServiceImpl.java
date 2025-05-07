package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.MandateService;
import com.cratechnologie.budget.domain.Mandate;
import com.cratechnologie.budget.repository.MandateRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Mandate}.
 */
@Service
@Transactional
public class MandateServiceImpl implements MandateService {

    private static final Logger LOG = LoggerFactory.getLogger(MandateServiceImpl.class);

    private final MandateRepository mandateRepository;

    public MandateServiceImpl(MandateRepository mandateRepository) {
        this.mandateRepository = mandateRepository;
    }

    @Override
    public Mandate save(Mandate mandate) {
        LOG.debug("Request to save Mandate : {}", mandate);
        return mandateRepository.save(mandate);
    }

    @Override
    public Mandate update(Mandate mandate) {
        LOG.debug("Request to update Mandate : {}", mandate);
        return mandateRepository.save(mandate);
    }

    @Override
    public Optional<Mandate> partialUpdate(Mandate mandate) {
        LOG.debug("Request to partially update Mandate : {}", mandate);

        return mandateRepository
            .findById(mandate.getId())
            .map(existingMandate -> {
                if (mandate.getMandateNumber() != null) {
                    existingMandate.setMandateNumber(mandate.getMandateNumber());
                }
                if (mandate.getMandateDate() != null) {
                    existingMandate.setMandateDate(mandate.getMandateDate());
                }
                if (mandate.getIssueSlipNumber() != null) {
                    existingMandate.setIssueSlipNumber(mandate.getIssueSlipNumber());
                }
                if (mandate.getMonthAndYearOfIssue() != null) {
                    existingMandate.setMonthAndYearOfIssue(mandate.getMonthAndYearOfIssue());
                }
                if (mandate.getSupportingDocuments() != null) {
                    existingMandate.setSupportingDocuments(mandate.getSupportingDocuments());
                }

                return existingMandate;
            })
            .map(mandateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mandate> findOne(Long id) {
        LOG.debug("Request to get Mandate : {}", id);
        return mandateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Mandate : {}", id);
        mandateRepository.deleteById(id);
    }
}
