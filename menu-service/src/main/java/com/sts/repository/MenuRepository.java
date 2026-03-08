package com.sts.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

}
