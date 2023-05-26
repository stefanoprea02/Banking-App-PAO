package services;

import config.DatabaseConfiguration;
import models.Account;
import models.CheckingAccount;
import models.Transaction;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistEra;
import java.util.*;

public class TransactionService {
    private static final AccountService accountService = new AccountService();

    public void transferMoney(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Sender info");
        Account sender = accountService.getAccountByIBAN();

        System.out.println("Receiver info");
        Account receiver = accountService.getAccountByIBAN();

        System.out.print("Enter sum : ");
        double sum = Double.parseDouble(scanner.nextLine());

        /*
        if(sender.getBalance() < sum){
            System.out.println("Not enough funds.");
            return;
        }
         */

        System.out.print("Enter description : ");
        String description = scanner.nextLine();

        String id = UUID.randomUUID().toString();
        LocalDate date = LocalDate.now();
        String type = "Money transfer";
        double commission = 0;

        String senderType = "";
        String receiverType = "";
        if(sender instanceof CheckingAccount)
            senderType = "senderCheckingAccount_id";
        else senderType = "senderSavingsAccount_id";
        if(receiver instanceof CheckingAccount)
            receiverType = "receiverCheckingAccount_id";
        else receiverType = "receiverSavingsAccount_id";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            String insertAccount = "INSERT INTO transaction(id, date, type, amount, description, commission, " + senderType + ", " + receiverType + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertAccount);
            preparedStatement.setString(1, id);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setString(3, type);
            preparedStatement.setDouble(4, sum);
            preparedStatement.setString(5, description);
            preparedStatement.setDouble(6, commission);
            preparedStatement.setString(7, sender.getId());
            preparedStatement.setString(8, receiver.getId());
            preparedStatement.executeUpdate();
            double newBalanceSender = sender.getBalance() - sum;
            double newBalanceReceiver = receiver.getBalance() + sum;

            if(senderType.equals("senderCheckingAccount_id")) {
                String updateSender = "UPDATE checkingaccount SET balance = ? WHERE IBAN = ?";
                preparedStatement = connection.prepareStatement(updateSender);
                preparedStatement.setDouble(1, newBalanceSender);
                preparedStatement.setString(2, sender.getIBAN());
                preparedStatement.executeUpdate();
            }else{
                String updateSender = "UPDATE savingsAccount SET balance = ? WHERE IBAN = ?";
                preparedStatement = connection.prepareStatement(updateSender);
                preparedStatement.setDouble(1, newBalanceSender);
                preparedStatement.setString(2, sender.getIBAN());
                preparedStatement.executeUpdate();
            }
            if(receiverType.equals("receiverCheckingAccount_id")) {
                String updateReceiver = "UPDATE checkingaccount SET balance = ? WHERE IBAN = ?";
                preparedStatement = connection.prepareStatement(updateReceiver);
                preparedStatement.setDouble(1, newBalanceReceiver);
                preparedStatement.setString(2, receiver.getIBAN());
                preparedStatement.executeUpdate();
            }else{
                String updateReceiver = "UPDATE savingsaccount SET balance = ? WHERE IBAN = ?";
                preparedStatement = connection.prepareStatement(updateReceiver);
                preparedStatement.setDouble(1, newBalanceReceiver);
                preparedStatement.setString(2, receiver.getIBAN());
                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Transaction> getAllTransactions(Optional<Date> startDate, Optional<Date> endDate){
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try {
            String select = "";
            if(startDate.isPresent()){
                select = "select * from transaction WHERE date between \'" + startDate.get() + "\' and \'" + endDate.get() + "\'";
            }
            else select = "select * from transaction";
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();

            Set<Transaction> transactionSet = new HashSet<>();
            Transaction transaction = mapToTransaction(resultSet);
            if(transaction != null) {
                transactionSet.add(transaction);
            }
            while (transaction != null){
                transaction = mapToTransaction(resultSet);
                if(transaction != null) {
                    transactionSet.add(transaction);
                }
            }
            return transactionSet;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Transaction mapToTransaction(ResultSet resultSet) throws SQLException {
        if(!resultSet.next())
            return null;
        return new Transaction(resultSet.getString(1),
                resultSet.getDate(2).toLocalDate(),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10),
                resultSet.getString(3),
                resultSet.getDouble(4),
                resultSet.getString(5),
                resultSet.getDouble(6));
    }
}
