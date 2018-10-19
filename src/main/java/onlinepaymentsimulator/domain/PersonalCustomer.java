package onlinepaymentsimulator.domain;

import java.util.List;


public class PersonalCustomer extends Customer {
    private String firstname;
    private String lastname;
    private String socialSecurityNumber;

    public PersonalCustomer(String firstname, String lastname, String socialSecurityNumber, String address, String phone, String email, List<User> user) {
        super(address, phone, email, user);
        this.firstname = firstname;
        this.lastname = lastname;
        this.socialSecurityNumber = socialSecurityNumber;
    }
    
    @Override
    public String getName() {
        return this.firstname + " " + this.lastname;
    }
}
