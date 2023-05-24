package models;

import java.time.LocalDate;
import java.util.TreeSet;

public class CheckingAccount extends Account {
    private double overdraftLimit;
    private double monthlyFee;

    public CheckingAccount(String id,
                           String IBAN,
                           double balance,
                           LocalDate dateOpened,
                           String individual_id,
                           String business_id,
                           double overdraftLimit,
                           double monthlyFee) {
        super(id, IBAN, balance, dateOpened, individual_id, business_id);
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @Override
    public String toString() {
        return "\nCheckingAccount{\n" + super.toString() +
                "\n    overdraftLimit=" + overdraftLimit +
                "\n    monthlyFee=" + monthlyFee +
                "\n}";
    }
}
