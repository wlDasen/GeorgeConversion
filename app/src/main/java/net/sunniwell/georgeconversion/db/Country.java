package net.sunniwell.georgeconversion.db;

/**
 * Created by admin on 2017/10/12.
 */

public class Country {
    private String countryName;
    private String countryCode;
    private String moneyNumber;
    private String moneySymbol;

    public Country(String countryName, String countryCode, String moneyNumber, String moneySymbol) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.moneyNumber = moneyNumber;
        this.moneySymbol = moneySymbol;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMoneyNumber() {
        return moneyNumber;
    }

    public void setMoneyNumber(String moneyNumber) {
        this.moneyNumber = moneyNumber;
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
