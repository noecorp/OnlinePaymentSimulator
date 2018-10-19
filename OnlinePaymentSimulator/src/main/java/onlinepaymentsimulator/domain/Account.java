package onlinepaymentsimulator.domain;


public class Account {
    private int id;
    private Balance balance;
    private String name;
    private User user;
    private String iban;

    public Account(int id, Balance balance, String name, User user, String iban) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.user = user;
        this.iban = iban;
    }

    public int getId() {
        return id;
    }

    public Balance getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIban() {
        return iban;
    }

    @Override
    public String toString() {
        return name + " [" + iban + "] (" + balance + ")";
    }
}
