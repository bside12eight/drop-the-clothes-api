package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByOrderByListOrderAsc();

    Optional<Category> findByName(String categoryName);
}
