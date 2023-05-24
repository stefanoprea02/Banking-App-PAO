package models;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.TreeSet;

public class Account {
    private final String id;
    private String IBAN;
    private double balance;
    private LocalDate dateOpened;
    private String individual_id;
    private String business_id;

    public Account(String id, String IBAN, double balance, LocalDate dateOpened, String individual_id, String business_id) {
        this.id = id;
        this.IBAN = IBAN;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.individual_id = individual_id;
        this.business_id = business_id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String accountNumber) {
        this.IBAN = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        String owner;
        if(individual_id != null)
            owner = individual_id;
        else
            owner = business_id;
        return
                "    IBAN=" + IBAN +
                "\n    balance=" + balance +
                "\n    dateOpened=" + dateOpened +
                "\n    owner=" + owner;
    }
}