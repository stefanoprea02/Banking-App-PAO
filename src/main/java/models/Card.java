package models;

import java.time.LocalDate;
import java.util.Date;

public class Card {
    private final String id;
    private String cardNumber;
    private String individual_id;
    private String business_id;
    private String savingsAccount_id;
    private String checkingAccount_id;
    private LocalDate expirationDate;
    private int isActive;

    public Card(String id, String cardNumber, LocalDate expirationDate, int isActive, String individual_id, String business_id, String savingsAccount_id, String checkingAccount_id) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.individual_id = individual_id;
        this.business_id = business_id;
        this.savingsAccount_id = savingsAccount_id;
        this.checkingAccount_id = checkingAccount_id;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int isActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    public String getIndividual_id() {
        return individual_id;
    }

    public void setIndividual_id(String individual_id) {
        this.individual_id = individual_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getSavingsAccount_id() {
        return savingsAccount_id;
    }

    public void setSavingsAccount_id(String savingsAccount_id) {
        this.savingsAccount_id = savingsAccount_id;
    }

    public String getCheckingAccount_id() {
        return checkingAccount_id;
    }

    public void setCheckingAccount_id(String checkingAccount_id) {
        this.checkingAccount_id = checkingAccount_id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        String account = "";
        if(checkingAccount_id != null)
            account = checkingAccount_id;
        else
            account = savingsAccount_id;
        return "Card{" +
                "\nid=" + id +
                "\ncardNumber=" + cardNumber +
                "\naccount=" + account +
                "\nexpirationDate=" + expirationDate +
                "\nisActive=" + isActive +
                "\n}";
    }
}
