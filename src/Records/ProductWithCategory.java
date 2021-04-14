package Records;

import DatabaseMock.Products;

public record ProductWithCategory(String ID, String name, Products products) {}