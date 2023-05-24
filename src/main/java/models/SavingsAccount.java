package models;

import java.time.LocalDate;
import java.util.TreeSet;

public class SavingsAccount extends Account{
    private double interestRate;
    private double minimumBalance;
    private double monthlyWithdrawalLimit;
    private double monthlyWithdrew;

    public SavingsAccount(String id,
                          String IBAN,
                          double balance,
                          LocalDate dateOpened,
                          String individual_id,
                          String business_id,
                          double interestRate,
                          double minimumBalance,
                          double monthlyWithdrawalLimit,
                          double monthlyWithdrew) {
        super(id, IBAN, balance, dateOpened, individual_id, business_id);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.monthlyWithdrawalLimit = monthlyWithdrawalLimit;
        this.monthlyWithdrew = monthlyWithdrew;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public double getMonthlyWithdrawalLimit() {
        return monthlyWithdrawalLimit;
    }

    public void setMonthlyWithdrawalLimit(double monthlyWithdrawalLimit) {
        this.monthlyWithdrawalLimit = monthlyWithdrawalLimit;
    }

    public double getMonthlyWithdrew() {
        return monthlyWithdrew;
    }

    public void setMonthlyWithdrew(double monthlyWithdrew) {
        this.monthlyWithdrew = monthlyWithdrew;
    }

    @Override
    public String toString() {
        return "\nSavingsAccount{\n" + super.toString() +
                "\n    interestRate=" + interestRate +
                "\n    minimumBalance=" + minimumBalance +
                "\n    monthlyWithdrawalLimit=" + monthlyWithdrawalLimit +
                "\n    monthlyWithdrew=" + monthlyWithdrew +
                "\n}";
    }
}
