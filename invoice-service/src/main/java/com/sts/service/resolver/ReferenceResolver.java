package com.sts.service.resolver;

import com.sts.exception.ResourceNotFoundException;
import com.sts.model.Invoice;
 import com.sts.repository.InvoiceRepository;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final InvoiceRepository invoiceRepository;


    public Invoice getOrThrow(UUID invoiceId) {
        return orThrow(
                invoiceRepository.findById(invoiceId),
                AppConstants.ERROR_MESSAGES.INVOICE_NOT_FOUND
        );
    }

    private <T> T orThrow(Optional<T> entity, String message) {
        return entity.orElseThrow(() -> new ResourceNotFoundException(message));
    }

}
