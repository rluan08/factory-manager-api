package com.luan.factorymanager.entity;

import jakarta.persistence.*;
import java.util.Map;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @ElementCollection
    @CollectionTable(name = "product_composition", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyJoinColumn(name = "raw_material_id")
    @Column(name = "required_quantity")
    private Map<RawMaterial, Double> composition;

    public Product() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Map<RawMaterial, Double> getComposition() { return composition; }
    public void setComposition(Map<RawMaterial, Double> composition) { this.composition = composition; }
}