package com.swd392.preOrderBlindBox.repository;

import com.swd392.preOrderBlindBox.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryId(Long parentId);

    Optional<Category> findByBlindboxSeriesId(Long blindboxSeriesId);
}
