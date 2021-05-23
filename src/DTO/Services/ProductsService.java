package DTO.Services;

import DTO.CategoryWithProducts;
import DTO.Product;
import Database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Audit.Audit;
import CurrentUser.CurrentUser;

public class ProductsService implements DTOService <Product> {

    private static ProductsService instance;

    private ProductsService() {}

    public static ProductsService getInstance() {
        if (instance == null) {
            instance = new ProductsService();
        }

        return instance;
    }

    @Override
    public Product selectByID(String ID) {
        return null;
    }

    public CategoryWithProducts selectByCategoryID(String ID) {
        Database database = Database.getInstance();

        String categoryName;

        ResultSet nameResponse = database.sendQuery("SELECT name" +
                " FROM product_categories" +
                " WHERE ID = UNHEX(?);", Collections.singletonList(ID));

        try {
            nameResponse.next();

            categoryName = nameResponse.getString("name");
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't find a category with the given ID: " + exception);
        }

        ResultSet response = database.sendQuery("SELECT HEX(ID) AS ID, name, ingredients, price" +
                " FROM products" +
                " WHERE category_id = UNHEX(?);", Collections.singletonList(ID));

        List <Product> products = new ArrayList <> ();

        try {
            while (response.next()) {
                Product product = new Product(response.getString("ID"),
                        response.getString("name"),
                        response.getString("ingredients"),
                        response.getDouble("price"));

                products.add(product);
            }
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't load products: " + exception);
        }

        return new CategoryWithProducts(ID, categoryName, products);
    }

    public List <CategoryWithProducts> selectAllCategoriesWithProducts() {
        Database database = Database.getInstance();

        ResultSet categoriesIDs = database.sendQuery("SELECT HEX(ID) AS ID" +
                " FROM product_categories;", Collections.emptyList());

        List <CategoryWithProducts> answer = new ArrayList <> ();

        try {
            while (categoriesIDs.next()) {
                CategoryWithProducts current = getInstance().selectByCategoryID(categoriesIDs.getString("ID"));

                answer.add(current);
            }
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't get product categories IDs: " + exception);
        }

        Audit.insertLog(CurrentUser.getUser().ID(), "Get all categories with products");

        return answer;
    }

    public List <Product> getAllProducts() {
        List <CategoryWithProducts> categories = selectAllCategoriesWithProducts();
        List <Product> answer = new ArrayList <> ();

        for (CategoryWithProducts category : categories) {
            answer.addAll(category.products());
        }

        Audit.insertLog(CurrentUser.getUser().ID(), "Get all products");

        return answer;
    }
}
