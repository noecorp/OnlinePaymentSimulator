package onlinepaymentsimulator.domain;

import java.util.List;


public class BusinessCustomer extends Customer {
    private String name;
    private BusinessType businessType;

    public BusinessCustomer(String name, BusinessType businessType, String address, String phone, String email, List<User> user) {
        super(address, phone, email, user);
        this.name = name;
        this.businessType = businessType;
    }

    @Override
    public String getName() {
        return name;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }
    
    
}
