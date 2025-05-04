package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.domain.Chapter;
import com.cratechnologie.budget.repository.ChapterRepository;
import com.cratechnologie.budget.service.ChapterService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Chapter}.
 */
@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {

    private static final Logger LOG = LoggerFactory.getLogger(ChapterServiceImpl.class);

    private final ChapterRepository chapterRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public Chapter save(Chapter chapter) {
        LOG.debug("Request to save Chapter : {}", chapter);
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter update(Chapter chapter) {
        LOG.debug("Request to update Chapter : {}", chapter);
        return chapterRepository.save(chapter);
    }

    @Override
    public Optional<Chapter> partialUpdate(Chapter chapter) {
        LOG.debug("Request to partially update Chapter : {}", chapter);

        return chapterRepository
            .findById(chapter.getId())
            .map(existingChapter -> {
                if (chapter.getCode() != null) {
                    existingChapter.setCode(chapter.getCode());
                }
                if (chapter.getDesignation() != null) {
                    existingChapter.setDesignation(chapter.getDesignation());
                }

                return existingChapter;
            })
            .map(chapterRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chapter> findOne(Long id) {
        LOG.debug("Request to get Chapter : {}", id);
        return chapterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Chapter : {}", id);
        chapterRepository.deleteById(id);
    }
}
