package DTO;

public record User(String ID, String username, String email, String password,
                   String phoneNumber, String country, String city, String street, String postalCode) {}