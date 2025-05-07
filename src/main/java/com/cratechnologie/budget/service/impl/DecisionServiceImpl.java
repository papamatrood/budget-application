package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.DecisionService;
import com.cratechnologie.budget.domain.Decision;
import com.cratechnologie.budget.repository.DecisionRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Decision}.
 */
@Service
@Transactional
public class DecisionServiceImpl implements DecisionService {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionServiceImpl.class);

    private final DecisionRepository decisionRepository;

    public DecisionServiceImpl(DecisionRepository decisionRepository) {
        this.decisionRepository = decisionRepository;
    }

    @Override
    public Decision save(Decision decision) {
        LOG.debug("Request to save Decision : {}", decision);
        return decisionRepository.save(decision);
    }

    @Override
    public Decision update(Decision decision) {
        LOG.debug("Request to update Decision : {}", decision);
        return decisionRepository.save(decision);
    }

    @Override
    public Optional<Decision> partialUpdate(Decision decision) {
        LOG.debug("Request to partially update Decision : {}", decision);

        return decisionRepository
            .findById(decision.getId())
            .map(existingDecision -> {
                if (decision.getDecisionNumber() != null) {
                    existingDecision.setDecisionNumber(decision.getDecisionNumber());
                }
                if (decision.getDecisionDate() != null) {
                    existingDecision.setDecisionDate(decision.getDecisionDate());
                }

                return existingDecision;
            })
            .map(decisionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Decision> findOne(Long id) {
        LOG.debug("Request to get Decision : {}", id);
        return decisionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Decision : {}", id);
        decisionRepository.deleteById(id);
    }
}
