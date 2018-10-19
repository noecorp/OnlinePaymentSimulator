package onlinepaymentsimulator.domain;

import java.util.Date;


public class Payment {
    private Balance sum;
    private Date created;
    private String message;
    private Account from;
    private String toIBAN;
    private Integer ref;

    public Payment(Balance sum, Date created, String message, Account from, String toIBAN, Integer ref) {
        this.sum = sum;
        this.created = created;
        this.message = message;
        this.from = from;
        this.toIBAN = toIBAN;
        this.ref = ref;
    }

    public Balance getSum() {
        return sum;
    }

    public Date getCreated() {
        return created;
    }

    public String getMessage() {
        return message;
    }

    public Account getFrom() {
        return from;
    }

    public String getToIBAN() {
        return toIBAN;
    }

    public Integer getRef() {
        return ref;
    }
    
    
}
