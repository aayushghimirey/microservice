package com.sts.service.specification;

import com.sts.dto.request.GetVendorQueryRequest;
import com.sts.model.vendor.Vendor;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorSpecification {


    public Specification<Vendor> buildSpecification(GetVendorQueryRequest req) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (req.name() != null && !req.name().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + req.name().toLowerCase() + "%"
                        )
                );
            }

            if (req.panNumber() != null && !req.panNumber().isBlank()) {
                predicates.add(
                        cb.equal(root.get("panNumber"), req.panNumber())
                );
            }

            if (req.contactNumber() != null && !req.contactNumber().isBlank()) {
                predicates.add(
                        cb.like(root.get("contactNumber"), "%" + req.contactNumber() + "%")
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
