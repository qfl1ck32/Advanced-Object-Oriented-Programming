package DTO;


import java.util.List;

public record Transaction(String ID, String timestamp, String status, Double price, List<TransactionItem> items) {}
