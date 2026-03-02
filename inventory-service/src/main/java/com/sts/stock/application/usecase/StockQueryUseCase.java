package com.sts.stock.application.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sts.stock.application.query.GetStockQueryRequest;
import com.sts.stock.application.query.GetVariantQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sts.stock.domain.model.Stock;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.domain.repository.StockRepository;
import com.sts.stock.domain.repository.StockVariantRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockQueryUseCase {

    private static final Logger log = LoggerFactory.getLogger(StockQueryUseCase.class);
    private final StockRepository stockRepository;
    private final StockVariantRepository stockVariantRepository;

    // Fetch all stocks with pagination
    public Page<Stock> getAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    // Fetch stocks with dynamic filters based on GetStockQueryRequest
    public Page<Stock> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {
        Specification<Stock> spec = buildSpecification(queryRequest);
        return stockRepository.findAll(spec, pageable);
    }

    // Fetch all variants with pagination
    public Page<StockVariant> getAllVariantByStockId(UUID stockId, Pageable pageable) {
        return stockVariantRepository.findAllByStockId(stockId, pageable);
    }

    // Fetch variants based on variant-specific filters
    public Page<StockVariant> getAllQueryVariantByStockId(
            UUID stockId,
            GetVariantQueryRequest request,
            Pageable pageable
    ) {
        Specification<StockVariant> spec = buildSpecification(request, stockId);
        return stockVariantRepository.findAll(spec, pageable);
    }

    // id validators
    public boolean validateVariantIdWithUnitId(UUID variantId, UUID unitId) {
        log.info("Validating variantId {} with unitId {}", variantId, unitId);
        Optional<StockVariant> stockVariant = stockVariantRepository.findById(variantId)
                .filter(variant -> variant.getUnits().stream()
                        .anyMatch(u -> u.getId().equals(unitId))
                );
        return stockVariant.isPresent();
    }


    // Specification for Stock filtering
    private Specification<Stock> buildSpecification(GetStockQueryRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.name() != null && !request.name().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.name().toLowerCase() + "%"));
            }
            if (request.type() != null) {
                predicates.add(cb.equal(root.get("type"), request.type()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Specification for StockVariant filtering
    private Specification<StockVariant> buildSpecification(
            GetVariantQueryRequest request,
            UUID stockId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Optional name filter
            if (request.variantName() != null && !request.variantName().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + request.variantName().toLowerCase() + "%"
                ));
            }

            // Mandatory stockId filter
            predicates.add(cb.equal(root.get("stock").get("id"), stockId));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}