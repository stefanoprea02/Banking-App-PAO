package models;

import java.time.LocalDate;

public class Transaction {
    private final String id;
    private String senderSavingsAccount_id;
    private String senderCheckingAccount_id;
    private String receiverSavingsAccount_id;
    private String receiverCheckingAccount_id;
    private LocalDate date;
    private String type;
    private double amount;
    private String description;
    private double commission;

    public Transaction(String id, LocalDate date, String senderSavingsAccount_id, String senderCheckingAccount_id, String receiverSavingsAccount_id, String receiverCheckingAccount_id, String type, double amount, String description, double commission) {
        this.id = id;
        this.senderSavingsAccount_id = senderSavingsAccount_id;
        this.senderCheckingAccount_id = senderCheckingAccount_id;
        this.receiverSavingsAccount_id = receiverSavingsAccount_id;
        this.receiverCheckingAccount_id = receiverCheckingAccount_id;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.commission = commission;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getSenderSavingsAccount_id() {
        return senderSavingsAccount_id;
    }

    public void setSenderSavingsAccount_id(String senderSavingsAccount_id) {
        this.senderSavingsAccount_id = senderSavingsAccount_id;
    }

    public String getSenderCheckingAccount_id() {
        return senderCheckingAccount_id;
    }

    public void setSenderCheckingAccount_id(String senderCheckingAccount_id) {
        this.senderCheckingAccount_id = senderCheckingAccount_id;
    }

    public String getReceiverSavingsAccount_id() {
        return receiverSavingsAccount_id;
    }

    public void setReceiverSavingsAccount_id(String receiverSavingsAccount_id) {
        this.receiverSavingsAccount_id = receiverSavingsAccount_id;
    }

    public String getReceiverCheckingAccount_id() {
        return receiverCheckingAccount_id;
    }

    public void setReceiverCheckingAccount_id(String receiverCheckingAccount_id) {
        this.receiverCheckingAccount_id = receiverCheckingAccount_id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        String senderS = "";
        String receiverS = "";
        if(getReceiverCheckingAccount_id() != null){
            receiverS = getReceiverCheckingAccount_id();
        }else{
            receiverS = getReceiverSavingsAccount_id();
        }
        if(getSenderCheckingAccount_id() != null){
            senderS = getSenderCheckingAccount_id();
        }else{
            senderS = getSenderSavingsAccount_id();
        }

        return "Transaction{" +
                "\n    id=" + id +
                "\n    sender=" + senderS +
                "\n    receiver=" + receiverS +
                "\n    date=" + date +
                "\n    type=" + type +
                "\n    amount=" + amount +
                "\n    description=" + description +
                "\n    commission=" + commission +
                "\n}";
    }
}
