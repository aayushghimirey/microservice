package com.sts.service.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.GetVariantQueryRequest;
import com.sts.dto.response.StockResponse;
import com.sts.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

@Service
@Slf4j
@AllArgsConstructor
public class StockQueryService {

    private final StockRepository stockRepository;
    private final StockVariantRepository stockVariantRepository;
    private final StockMapper stockMapper;

    // Fetch all stocks with pagination
    public Page<StockResponse> getAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable).map(stockMapper::toResponse);
    }

    // Fetch stocks with dynamic filters based on GetStockQueryRequest
    public Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {
        Specification<Stock> spec = buildSpecification(queryRequest);
        return stockRepository.findAll(spec, pageable).map(stockMapper::toResponse);
    }

    // Fetch all variants with pagination
    public Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable) {
        return stockVariantRepository.findAllByStockId(stockId, pageable).map(stockMapper::toVariantResponse);
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


}