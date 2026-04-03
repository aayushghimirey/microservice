package com.sts.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinanceService<R> {

    Page<R> getAllRecords(Pageable pageable);

}
