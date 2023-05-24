package services;

import config.DatabaseConfiguration;
import models.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatementService {
    private static final AccountService accountService = new AccountService();
    private static final TransactionService transactionService = new TransactionService();

    public void createStatement() {
        Scanner scanner = new Scanner(System.in);

        String accountType = accountService.getAccountType(scanner);
        Account account = accountService.getAccountByIBAN();
        if (account == null) {
            System.out.println("There is no account with this IBAN");
            return;
        }

        accountType += "Account_id";
        String customerId = "";
        String customerType = "";
        if (account.getBusiness_id() != null) {
            customerId = account.getBusiness_id();
            customerType = "business_id";
        }
        else {
            customerId = account.getIndividual_id();
            customerType = "individual_id";
        }

        String regex = "^\\d{2} \\d{2} \\d{4}$";
        Pattern pattern = Pattern.compile(regex);

        System.out.print("Enter starting date format dd mm yyyy : ");
        String startDateStr = scanner.nextLine();
        Matcher matcher = pattern.matcher(startDateStr);
        while(!matcher.matches()){
            System.out.print("Enter starting date format dd mm yyyy : ");
            startDateStr = scanner.nextLine();
            matcher = pattern.matcher(startDateStr);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);

        System.out.print("Enter ending date format dd mm yyyy : ");
        String endingDateStr = scanner.nextLine();
        matcher = pattern.matcher(endingDateStr);
        while(!matcher.matches()){
            System.out.print("Enter ending date format dd mm yyyy : ");
            endingDateStr = scanner.nextLine();
            matcher = pattern.matcher(endingDateStr);
        }
        LocalDate endDate = LocalDate.parse(endingDateStr, formatter);

        String statementId = UUID.randomUUID().toString();
        String transactionStatementId = UUID.randomUUID().toString();
        LocalDate statementDate = LocalDate.now();

        Set<Transaction> transactions;
        transactions = transactionService.getAllTransactions(Optional.of(Date.valueOf(startDate)), Optional.of(Date.valueOf(endDate)));

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            String addStatement = "insert into statement(id, statementDate, startDate, endDate, " + accountType + ", " + customerType + ") values(?, ?, ?, ?, ? ,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(addStatement);
            preparedStatement2.setString(1, statementId);
            preparedStatement2.setDate(2, Date.valueOf(statementDate));
            preparedStatement2.setDate(3, Date.valueOf(startDate));
            preparedStatement2.setDate(4, Date.valueOf(endDate));
            preparedStatement2.setString(5, account.getId());
            preparedStatement2.setString(6, customerId);
            preparedStatement2.executeUpdate();

            String query = "insert into transactionstatement(id, transaction_id, statement_id) values(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if(transactions == null)
                return;
            for(Transaction transaction : transactions){
                preparedStatement.setString(1, transactionStatementId);
                preparedStatement.setString(2, transaction.getId());
                preparedStatement.setString(3, statementId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getStatements(){
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            String query = "select * from statement";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Statement statement = mapToStatement(resultSet);
            while(statement != null){
                System.out.println(statement);
                String query2 = "select * from transaction where id in (select transaction_id from transactionstatement where statement_id = ?)";
                preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setString(1, statement.getId());
                ResultSet resultSet1 = preparedStatement.executeQuery();
                Transaction transaction = transactionService.mapToTransaction(resultSet1);
                while(transaction != null){
                    System.out.print(transaction);
                    transaction = transactionService.mapToTransaction(resultSet1);
                }
                System.out.println("\n}");
                statement = mapToStatement(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Statement mapToStatement(ResultSet resultSet) throws SQLException {
        if(!resultSet.next())
            return null;
        return new Statement(resultSet.getString(1),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getDate(2).toLocalDate(),
                resultSet.getDate(3).toLocalDate(),
                resultSet.getDate(4).toLocalDate());
    }
}
