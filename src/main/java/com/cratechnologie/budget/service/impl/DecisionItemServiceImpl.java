package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.DecisionItemService;
import com.cratechnologie.budget.domain.DecisionItem;
import com.cratechnologie.budget.repository.DecisionItemRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.DecisionItem}.
 */
@Service
@Transactional
public class DecisionItemServiceImpl implements DecisionItemService {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionItemServiceImpl.class);

    private final DecisionItemRepository decisionItemRepository;

    public DecisionItemServiceImpl(DecisionItemRepository decisionItemRepository) {
        this.decisionItemRepository = decisionItemRepository;
    }

    @Override
    public DecisionItem save(DecisionItem decisionItem) {
        LOG.debug("Request to save DecisionItem : {}", decisionItem);
        return decisionItemRepository.save(decisionItem);
    }

    @Override
    public DecisionItem update(DecisionItem decisionItem) {
        LOG.debug("Request to update DecisionItem : {}", decisionItem);
        return decisionItemRepository.save(decisionItem);
    }

    @Override
    public Optional<DecisionItem> partialUpdate(DecisionItem decisionItem) {
        LOG.debug("Request to partially update DecisionItem : {}", decisionItem);

        return decisionItemRepository
            .findById(decisionItem.getId())
            .map(existingDecisionItem -> {
                if (decisionItem.getBeneficiary() != null) {
                    existingDecisionItem.setBeneficiary(decisionItem.getBeneficiary());
                }
                if (decisionItem.getAmount() != null) {
                    existingDecisionItem.setAmount(decisionItem.getAmount());
                }
                if (decisionItem.getObservation() != null) {
                    existingDecisionItem.setObservation(decisionItem.getObservation());
                }

                return existingDecisionItem;
            })
            .map(decisionItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DecisionItem> findOne(Long id) {
        LOG.debug("Request to get DecisionItem : {}", id);
        return decisionItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DecisionItem : {}", id);
        decisionItemRepository.deleteById(id);
    }
}
