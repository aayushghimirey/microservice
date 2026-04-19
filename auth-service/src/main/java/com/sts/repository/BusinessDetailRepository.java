package com.sts.repository;

import com.sts.model.BusinessDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessDetailRepository extends JpaRepository<BusinessDetail, UUID> {
}
