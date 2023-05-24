package services;

import config.DatabaseConfiguration;
import models.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CardService {
    private static final CustomerService customerService = new CustomerService();
    private static final AccountService accountService = new AccountService();

    public void createCard(){
        Scanner scanner = new Scanner(System.in);
        String accountType = "";
        Account account = accountService.getAccountByIBAN();
        if(account instanceof SavingsAccount)
            accountType = "savingsAccount_id";
        if(account instanceof CheckingAccount)
            accountType = "checkingAccount_id";
        String customerType = "";
        String customerId = "";
        if(account.getBusiness_id() != null){
            customerType = "business_id";
            customerId = account.getBusiness_id();
        }else{
            customerType = "individual_id";
            customerId = account.getIndividual_id();
        }

        String id = UUID.randomUUID().toString();
        Random random = new Random();
        long nr = random.nextLong() % 10000000000000000L;
        String cardNumber = Long.toString(nr);
        LocalDate expirationDate = LocalDate.now();
        expirationDate = expirationDate.plusYears(4L);
        int isActive = 1;

        try {
            Connection connection = DatabaseConfiguration.getDatabaseConnection();
            String insertAccount = "INSERT INTO card(id, cardNumber, expirationDate, isActive, " + customerType + ", " + accountType + ") VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertAccount);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.setDate(3, Date.valueOf(expirationDate));
            preparedStatement.setInt(4, isActive);
            preparedStatement.setString(5, customerId);
            preparedStatement.setString(6, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCards(){
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            String select = "select * from card";
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();

            Card card = mapToCard(resultSet);
            while (card != null) {
                System.out.println(card.toString());
                card = mapToCard(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCard(){
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the card:");
        String id = scanner.nextLine();

        try{
            String select = "delete from card where id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Card mapToCard(ResultSet resultSet) throws SQLException{
        if(!resultSet.next())
            return null;
        return new Card(resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getDate(3).toLocalDate(),
                resultSet.getInt(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8));
    }
}
