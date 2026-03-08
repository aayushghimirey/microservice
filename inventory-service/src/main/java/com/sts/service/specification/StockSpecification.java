package com.sts.service.specification;

import com.sts.dto.request.GetStockQueryRequest;
import com.sts.model.stock.Stock;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockSpecification {

    // Specification for Stock filtering
    public Specification<Stock> buildSpecification(GetStockQueryRequest request) {
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
