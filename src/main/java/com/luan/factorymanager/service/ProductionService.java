package com.luan.factorymanager.service;

import com.luan.factorymanager.entity.Product;
import com.luan.factorymanager.entity.RawMaterial;
import com.luan.factorymanager.repository.ProductRepository;
import com.luan.factorymanager.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductionService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    public Map<String, Integer> calculateBestProduction() {
        List<Product> products = productRepository.findAll();
        List<RawMaterial> inventory = rawMaterialRepository.findAll();

        products.sort((p1, p2) -> {
            double totalQtyP1 = p1.getComposition().values().stream().mapToDouble(Double::doubleValue).sum();
            double totalQtyP2 = p2.getComposition().values().stream().mapToDouble(Double::doubleValue).sum();

            Double eff1 = totalQtyP1 > 0 ? p1.getPrice() / totalQtyP1 : 0.0;
            Double eff2 = totalQtyP2 > 0 ? p2.getPrice() / totalQtyP2 : 0.0;

            System.out.println("Produto: " + p1.getName() + " | Eficiência: " + eff1);
            System.out.println("Produto: " + p2.getName() + " | Eficiência: " + eff2);

            return eff2.compareTo(eff1); 
        });

        Map<String, Integer> productionPlan = new LinkedHashMap<>();
        
        Map<Long, Double> tempStock = new HashMap<>();
        for (RawMaterial rm : inventory) {
            tempStock.put(rm.getId(), rm.getQuantity());
        }

        for (Product product : products) {
            int count = 0;
            boolean canProduceUnit = true;

            while (canProduceUnit) {
                for (Map.Entry<RawMaterial, Double> entry : product.getComposition().entrySet()) {
                    Long materialId = entry.getKey().getId();
                    Double needed = entry.getValue();
                    if (tempStock.getOrDefault(materialId, 0.0) < needed) {
                        canProduceUnit = false;
                        break;
                    }
                }

                if (canProduceUnit) {
                    for (Map.Entry<RawMaterial, Double> entry : product.getComposition().entrySet()) {
                        Long materialId = entry.getKey().getId();
                        tempStock.put(materialId, tempStock.get(materialId) - entry.getValue());
                    }
                    count++;
                }
            }
            
            if (count > 0) {
                productionPlan.put(product.getName(), count);
            }
        }

        return productionPlan;
    }
}