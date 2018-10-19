package onlinepaymentsimulator.domain;

import java.util.List;


public class User {
    private int id;
    private int username;
    private List<Account> accounts;
    private Customer customer;

    public User(int id, int username, List<Account> accounts) {
        this.id = id;
        this.username = username;
        this.accounts = accounts;
    }

    public User(int id, int username, Customer customer) {
        this.id = id;
        this.username = username;
        this.customer = customer;
    }

    public User(int id, int username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    
}
