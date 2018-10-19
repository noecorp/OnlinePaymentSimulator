package onlinepaymentsimulator.domain;


public enum CurrencyType {
    EUR ("€"),
    USD ("$");
    
    private String currency;
 
    CurrencyType(String envCurrency) {
        this.currency = envCurrency;
    }
 
    public String getMarking() {
        return this.currency;
    }
}
