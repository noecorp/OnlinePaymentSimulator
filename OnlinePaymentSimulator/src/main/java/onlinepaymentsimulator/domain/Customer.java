package onlinepaymentsimulator.domain;

import java.util.List;


public class Customer {
    private String address;
    private String phone;
    private String email;
    private List<User> user;

    public Customer(String address, String phone, String email, List<User> user) {
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.user = user;
    }
    
    public String getName() {
        return "";
    }
}
