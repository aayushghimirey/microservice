package com.sts.repository;

import com.sts.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

 public interface MenuRepository extends JpaRepository<Menu, UUID> {


    boolean existsByCode(String code);


    Page<Menu> findAllByDeletedFalse(Pageable pageable);
}
