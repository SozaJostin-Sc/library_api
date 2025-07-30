package com.personal.library.library_api.service;

import com.personal.library.library_api.dto.category.CreateCategoryDTO;
import com.personal.library.library_api.dto.category.UpdateCategoryDTO;
import com.personal.library.library_api.exception.categories.*;
import com.personal.library.library_api.model.Categories;
import com.personal.library.library_api.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /// GET ALL CATEGORIES
    @Cacheable(value = "categories", key = "'all'")
    public List<Categories> getAll(){
        return categoryRepository.findAllByOrderByCategoryIdAsc();
    }

    /// GET CATEGORY BY ID
    @Cacheable(value = "categories", key = "#id")
    public Categories getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    /// CREATE CATEGORY
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Categories create(CreateCategoryDTO dto){
        validateCategoryNameUniqueness(dto.getName(), null);

        Categories category = Categories.builder()
                .name(dto.getName())
                .description(dto.getDescription() != null ? dto.getDescription().trim() : "")
                .status(true)
                .build();

        return categoryRepository.save(category);
    }

    /// UPDATE CATEGORIES
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#id"),
            @CacheEvict(value = "categories", key = "'all'")
    })
    public Categories update(Long id, UpdateCategoryDTO dto){
        if(dto == null){
            throw new CategoryEmptyException("Update category dto cannot be null");
        }

        Categories updateCategories = getById(id);

        if((dto.getName() == null || dto.getName().trim().isEmpty()) &&  (dto.getDescription() == null ||
                dto.getDescription().trim().isEmpty())){
            throw new CategoryEmptyException("At least one field (name or description) must be provided to update");
        }

        if(dto.getName() != null && !dto.getName().trim().isEmpty()) {
            validateCategoryNameUniqueness(dto.getName(), id);
            updateCategories.setName(dto.getName().trim());
        }

        if(dto.getDescription() != null) updateCategories.setDescription(dto.getDescription().trim());

        log.info("Category with id {} successfully updated", id);
        return categoryRepository.save(updateCategories);
    }


    /// DELETE CATEGORY
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public void delete(Long id){
        Categories deleteCategories = getById(id);
        if(Boolean.TRUE.equals(deleteCategories.getStatus())){
            deleteCategories.setStatus(false);
            categoryRepository.save(deleteCategories);
            log.info("Category with id {} successfully deleted", id);
        }else{
            throw new CategoryAlreadyDeletedException(id);
        }
    }

    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public void activate(Long id){
        Categories activateCategories = getById(id);

        if(Boolean.TRUE.equals(activateCategories.getStatus())){
            throw new CategoryAlreadyActiveException("Categories with id " + id + " is already activated");
        }

        activateCategories.setStatus(true);
        categoryRepository.save(activateCategories);
        log.info("Category with id {} successfully activated", id);
    }

    /// === PRIVATE METHODS ===

    private void validateCategoryNameUniqueness(String title, Long excludedId) {
        boolean exists = (excludedId == null)
                ? categoryRepository.existsByNameIgnoreCase(title)
                : categoryRepository.existsByNameIgnoreCaseAndCategoryIdNot(title, excludedId);

        if (exists) {
            log.warn("Category title {} already exists (excludedId: {})", title, excludedId);
            throw new CategoryDuplicateException(title);
        }
    }
}
