package com.sts.useCase;

import com.sts.domain.model.Menu;
import com.sts.domain.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class QueryMenuUseCase {

    private final MenuRepository menuRepository;


    @Transactional(readOnly = true)
    public Page<Menu> getAllMenus(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Menu getMenuById(UUID menuId) {
        return menuRepository.findById(menuId).orElse(null);
    }

}
