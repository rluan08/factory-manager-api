package com.luan.factorymanager.controller;

import com.luan.factorymanager.entity.Product;
import com.luan.factorymanager.entity.ProductDTO;
import com.luan.factorymanager.entity.RawMaterial;
import com.luan.factorymanager.repository.ProductRepository;
import com.luan.factorymanager.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }

    @PostMapping(consumes = "application/json")
    public Product create(@RequestBody ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name);
        product.setPrice(dto.price);

        Map<RawMaterial, Double> actualComposition = new HashMap<>();

        if (dto.composition != null) {
            dto.composition.forEach((id, quantity) -> {
                RawMaterial rm = rawMaterialRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Matéria-prima não encontrada com ID: " + id));
                actualComposition.put(rm, quantity);
            });
        }

        product.setComposition(actualComposition);
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}