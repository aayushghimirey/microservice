package com.sts.service;

import com.sts.dto.FinanceServiceInfo;
import com.sts.enums.DateSelection;
import com.sts.filter.TenantHolder;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.repository.PurchaseRecordRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OverviewFinanceService {

    private final InvoiceRecordRepository invoiceRecordRepository;
    private final PurchaseRecordRepository purchaseRecordRepository;
    private final RlsContext rlsContext;

    @Transactional
    public FinanceServiceInfo financeServiceInfo(DateSelection dateSelection) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        ZoneId zone = ZoneId.of("Asia/Kathmandu");

        Instant from = switch (dateSelection) {
            case TODAY -> LocalDate.now(zone)
                    .atStartOfDay(zone)
                    .toInstant();

            case WEEK -> Instant.now().minus(7, ChronoUnit.DAYS);

            case MONTH -> Instant.now().minus(30, ChronoUnit.DAYS);
        };

        BigDecimal invoiceRev =
                invoiceRecordRepository.sumGrossTotal(from);

        BigDecimal vatAmount =
                purchaseRecordRepository.sumVatAmountTotal(from);

        BigDecimal purchaseExp =
                purchaseRecordRepository.sumGrossTotal(from);

        return new FinanceServiceInfo(purchaseExp, invoiceRev, vatAmount);
    }
}
