package com.sts.api;

import com.sts.command.CreateMenuCommand;
import com.sts.dto.MenuResponseHandler;
import com.sts.event.MenuResponseDto;
import com.sts.useCase.CreateMenuUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuCommandController {

    private final CreateMenuUseCase createMenuUseCase;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody CreateMenuCommand command) {
        var response = createMenuUseCase.createMenu(command);
        return ResponseEntity.ok(MenuResponseHandler.from(response));
    }

}
