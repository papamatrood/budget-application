package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.AnnexDecisionService;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.repository.AnnexDecisionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.AnnexDecision}.
 */
@Service
@Transactional
public class AnnexDecisionServiceImpl implements AnnexDecisionService {

    private static final Logger LOG = LoggerFactory.getLogger(AnnexDecisionServiceImpl.class);

    private final AnnexDecisionRepository annexDecisionRepository;

    public AnnexDecisionServiceImpl(AnnexDecisionRepository annexDecisionRepository) {
        this.annexDecisionRepository = annexDecisionRepository;
    }

    @Override
    public AnnexDecision save(AnnexDecision annexDecision) {
        LOG.debug("Request to save AnnexDecision : {}", annexDecision);
        return annexDecisionRepository.save(annexDecision);
    }

    @Override
    public AnnexDecision update(AnnexDecision annexDecision) {
        LOG.debug("Request to update AnnexDecision : {}", annexDecision);
        return annexDecisionRepository.save(annexDecision);
    }

    @Override
    public Optional<AnnexDecision> partialUpdate(AnnexDecision annexDecision) {
        LOG.debug("Request to partially update AnnexDecision : {}", annexDecision);

        return annexDecisionRepository
            .findById(annexDecision.getId())
            .map(existingAnnexDecision -> {
                if (annexDecision.getDesignation() != null) {
                    existingAnnexDecision.setDesignation(annexDecision.getDesignation());
                }
                if (annexDecision.getExpenseAmount() != null) {
                    existingAnnexDecision.setExpenseAmount(annexDecision.getExpenseAmount());
                }
                if (annexDecision.getCreditsAlreadyOpen() != null) {
                    existingAnnexDecision.setCreditsAlreadyOpen(annexDecision.getCreditsAlreadyOpen());
                }
                if (annexDecision.getCreditsOpen() != null) {
                    existingAnnexDecision.setCreditsOpen(annexDecision.getCreditsOpen());
                }

                return existingAnnexDecision;
            })
            .map(annexDecisionRepository::save);
    }

    /**
     *  Get all the annexDecisions where Expense is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnnexDecision> findAllWhereExpenseIsNull() {
        LOG.debug("Request to get all annexDecisions where Expense is null");
        return StreamSupport.stream(annexDecisionRepository.findAll().spliterator(), false)
            .filter(annexDecision -> annexDecision.getExpense() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnnexDecision> findOne(Long id) {
        LOG.debug("Request to get AnnexDecision : {}", id);
        return annexDecisionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AnnexDecision : {}", id);
        annexDecisionRepository.deleteById(id);
    }
}
