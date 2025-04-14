package com.splitmate.model;

public class User extends AbstractModel {
    private String name;
    private String email;
    private String password;  // In production, password should be hashed.
    private String iban;
    
    public User(String name, String email, String password, String iban) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.iban = iban;
    }
    
    // Simple authentication (for demonstration only)
    public boolean authenticate(String passwordInput) {
        return this.password.equals(passwordInput);
    }
    
    public void updateProfile(String newName, String newIban) {
        this.name = newName;
        this.iban = newIban;
    }
    
    // Getters and setters...
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getIban() { return iban; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setIban(String iban) { this.iban = iban; }
}
