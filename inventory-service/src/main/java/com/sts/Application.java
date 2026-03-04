package com.sts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
        scanBasePackages = {"com.sts"}
)
@EnableJpaAuditing
public class InventoryService {
    public static void main(String[] args) {
        SpringApplication.run(InventoryService.class, args);
    }
}