package com.sts.api;

import com.sts.command.CreateTableCommand;
import com.sts.dto.TableResponse;
import com.sts.useCase.TableCommandUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table")
@AllArgsConstructor
public class TableCommandController {

    private final TableCommandUseCase tableCommandUseCase;


    @PostMapping
    public ResponseEntity<TableResponse> createTable(@RequestBody CreateTableCommand command) {
        var response = tableCommandUseCase.createTable(command);
        return ResponseEntity.ok(TableResponse.fromEntity(response));

    }
}
