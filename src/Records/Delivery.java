package Records;

import DatabaseMock.ProductsWithQuantity;

public record Delivery(String ID, ProductsWithQuantity productsWithQuantity){}