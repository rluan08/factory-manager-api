package com.luan.factorymanager.controller;

import com.luan.factorymanager.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    @GetMapping("/suggest")
    public Map<String, Integer> suggestProduction() {
        return productionService.calculateBestProduction();
    }
}