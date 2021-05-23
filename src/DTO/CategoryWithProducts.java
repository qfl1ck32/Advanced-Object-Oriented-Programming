package DTO;

import java.util.List;

public record CategoryWithProducts(String ID, String name, List<Product> products) {}
