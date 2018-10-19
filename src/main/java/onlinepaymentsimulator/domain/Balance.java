package onlinepaymentsimulator.domain;

import static onlinepaymentsimulator.domain.CurrencyType.EUR;
import onlinepaymentsimulator.utils.StringUtils;


public class Balance {
    private int eur;
    private int cent;
    private CurrencyType currencyType;

    public Balance(int eur, int cent, CurrencyType currencyType) {
        this.eur = eur;
        this.cent = cent;
        this.currencyType = currencyType;
    }

    public int getEur() {
        return eur;
    }

    public void setEur(int eur) {
        this.eur = eur;
    }

    public int getCent() {
        return cent;
    }

    public void setCent(int cent) {
        this.cent = cent;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
    
    public static Balance fromString(String balance) {
        String[] pieces = balance.split((balance.indexOf(",") > 0) ? "," : "\\.");
        
        return new Balance(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1]), EUR);
    }
    
    @Override
    public String toString() {
        return eur + "," + StringUtils.addLeadingZero(cent) + " " + currencyType.getMarking();
    }
}
