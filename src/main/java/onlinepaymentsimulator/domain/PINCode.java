package onlinepaymentsimulator.domain;


public class PINCode {
    private final Integer bank_code;
    private final Integer customer_code;
    private final User user;

    public PINCode(Integer bank_code, Integer customer_code, User user) {
        this.bank_code = bank_code;
        this.customer_code = customer_code;
        this.user = user;
    }

    public Integer getBankCode() {
        return bank_code;
    }

    public Integer getCustomerCode() {
        return customer_code;
    }

    public User getUser() {
        return user;
    }
    
}
