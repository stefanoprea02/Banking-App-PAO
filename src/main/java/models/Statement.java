package models;

import java.time.LocalDate;

public class Statement {
    private final String id;
    private String senderSavingsAccount_id;
    private String senderCheckingAccount_id;
    private String receiverSavingsAccount_id;
    private String receiverCheckingAccount_id;
    private LocalDate statementDate;
    private LocalDate startDate;
    private LocalDate endDate;

    public Statement(String id,
                     String senderSavingsAccount_id, String senderCheckingAccount_id, String receiverSavingsAccount_id, String receiverCheckingAccount_id, LocalDate statementDate,
                     LocalDate startDate,
                     LocalDate endDate) {
        this.id = id;
        this.senderSavingsAccount_id = senderSavingsAccount_id;
        this.senderCheckingAccount_id = senderCheckingAccount_id;
        this.receiverSavingsAccount_id = receiverSavingsAccount_id;
        this.receiverCheckingAccount_id = receiverCheckingAccount_id;
        this.statementDate = statementDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public LocalDate getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(LocalDate statementDate) {
        this.statementDate = statementDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "\nStatement{" +
                "\n    id=" + id +
                "\n    statementDate=" + statementDate +
                "\n    startDate=" + startDate +
                "\n    endDate=" + endDate;
    }
}
