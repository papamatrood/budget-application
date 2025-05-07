package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.SubTitleService;
import com.cratechnologie.budget.domain.SubTitle;
import com.cratechnologie.budget.repository.SubTitleRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.SubTitle}.
 */
@Service
@Transactional
public class SubTitleServiceImpl implements SubTitleService {

    private static final Logger LOG = LoggerFactory.getLogger(SubTitleServiceImpl.class);

    private final SubTitleRepository subTitleRepository;

    public SubTitleServiceImpl(SubTitleRepository subTitleRepository) {
        this.subTitleRepository = subTitleRepository;
    }

    @Override
    public SubTitle save(SubTitle subTitle) {
        LOG.debug("Request to save SubTitle : {}", subTitle);
        return subTitleRepository.save(subTitle);
    }

    @Override
    public SubTitle update(SubTitle subTitle) {
        LOG.debug("Request to update SubTitle : {}", subTitle);
        return subTitleRepository.save(subTitle);
    }

    @Override
    public Optional<SubTitle> partialUpdate(SubTitle subTitle) {
        LOG.debug("Request to partially update SubTitle : {}", subTitle);

        return subTitleRepository
            .findById(subTitle.getId())
            .map(existingSubTitle -> {
                if (subTitle.getCode() != null) {
                    existingSubTitle.setCode(subTitle.getCode());
                }
                if (subTitle.getDesignation() != null) {
                    existingSubTitle.setDesignation(subTitle.getDesignation());
                }

                return existingSubTitle;
            })
            .map(subTitleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubTitle> findOne(Long id) {
        LOG.debug("Request to get SubTitle : {}", id);
        return subTitleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubTitle : {}", id);
        subTitleRepository.deleteById(id);
    }
}
