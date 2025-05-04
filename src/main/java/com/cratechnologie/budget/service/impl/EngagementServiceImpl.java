package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.repository.EngagementRepository;
import com.cratechnologie.budget.service.EngagementService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Engagement}.
 */
@Service
@Transactional
public class EngagementServiceImpl implements EngagementService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementServiceImpl.class);

    private final EngagementRepository engagementRepository;

    public EngagementServiceImpl(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

    @Override
    public Engagement save(Engagement engagement) {
        LOG.debug("Request to save Engagement : {}", engagement);
        return engagementRepository.save(engagement);
    }

    @Override
    public Engagement update(Engagement engagement) {
        LOG.debug("Request to update Engagement : {}", engagement);
        return engagementRepository.save(engagement);
    }

    @Override
    public Optional<Engagement> partialUpdate(Engagement engagement) {
        LOG.debug("Request to partially update Engagement : {}", engagement);

        return engagementRepository
            .findById(engagement.getId())
            .map(existingEngagement -> {
                if (engagement.getEngagementNumber() != null) {
                    existingEngagement.setEngagementNumber(engagement.getEngagementNumber());
                }
                if (engagement.getEngagementDate() != null) {
                    existingEngagement.setEngagementDate(engagement.getEngagementDate());
                }
                if (engagement.getObjectOfExpense() != null) {
                    existingEngagement.setObjectOfExpense(engagement.getObjectOfExpense());
                }
                if (engagement.getNotifiedCredits() != null) {
                    existingEngagement.setNotifiedCredits(engagement.getNotifiedCredits());
                }
                if (engagement.getCreditCommitted() != null) {
                    existingEngagement.setCreditCommitted(engagement.getCreditCommitted());
                }
                if (engagement.getCreditsAvailable() != null) {
                    existingEngagement.setCreditsAvailable(engagement.getCreditsAvailable());
                }
                if (engagement.getAmountProposedCommitment() != null) {
                    existingEngagement.setAmountProposedCommitment(engagement.getAmountProposedCommitment());
                }
                if (engagement.getHeadDaf() != null) {
                    existingEngagement.setHeadDaf(engagement.getHeadDaf());
                }
                if (engagement.getFinancialController() != null) {
                    existingEngagement.setFinancialController(engagement.getFinancialController());
                }
                if (engagement.getGeneralManager() != null) {
                    existingEngagement.setGeneralManager(engagement.getGeneralManager());
                }

                return existingEngagement;
            })
            .map(engagementRepository::save);
    }

    /**
     *  Get all the engagements where Decision is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Engagement> findAllWhereDecisionIsNull() {
        LOG.debug("Request to get all engagements where Decision is null");
        return StreamSupport.stream(engagementRepository.findAll().spliterator(), false)
            .filter(engagement -> engagement.getDecision() == null)
            .toList();
    }

    /**
     *  Get all the engagements where Mandate is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Engagement> findAllWhereMandateIsNull() {
        LOG.debug("Request to get all engagements where Mandate is null");
        return StreamSupport.stream(engagementRepository.findAll().spliterator(), false)
            .filter(engagement -> engagement.getMandate() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Engagement> findOne(Long id) {
        LOG.debug("Request to get Engagement : {}", id);
        return engagementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Engagement : {}", id);
        engagementRepository.deleteById(id);
    }
}
