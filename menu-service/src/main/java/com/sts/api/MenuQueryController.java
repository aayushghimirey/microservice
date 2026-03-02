package com.sts.api;

import com.sts.domain.model.Menu;
import com.sts.dto.MenuResponseHandler;
import com.sts.event.MenuResponseDto;
import com.sts.pagination.PageRequestDto;
import com.sts.useCase.QueryMenuUseCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuQueryController {

    private final QueryMenuUseCase queryMenuUseCase;


    @GetMapping
    public ResponseEntity<Page<MenuResponseDto>> getAllMenus(@ModelAttribute PageRequestDto pageRequestDto) {

        Page<Menu> allMenus = queryMenuUseCase.getAllMenus(pageRequestDto.buildPageable());

        return ResponseEntity.ok(allMenus.map(MenuResponseHandler::from));

    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenuById(@PathVariable("menuId") UUID menuId) {
        return ResponseEntity.ok(MenuResponseHandler.from(queryMenuUseCase.getMenuById(menuId)));
    }
}
