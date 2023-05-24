package services;

import config.DatabaseConfiguration;
import models.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService {
    private static final CustomerService customerService = new CustomerService();

    public void createAccount(){
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        String id = UUID.randomUUID().toString();
        String IBAN = generateIBAN();
        double balance = 0;
        LocalDate dateOpened = LocalDate.now();

        String customerType = customerService.getCustomerType(scanner);

        System.out.print("Enter your name : ");
        Customer customer = customerService.getCustomerByName(customerType);

        String type = getAccountType(scanner);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            if (type.equals("checking")) {
                customerType += "_id";
                String insertAccount = "INSERT INTO checkingAccount(id, IBAN, balance, dateOpened, " + customerType + ", overdraftLimit, monthlyFee) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertAccount);
                System.out.print("Enter overdraft limit : ");
                double overdraftLimit = scanner.nextDouble();
                System.out.print("Enter monthly fee : ");
                double monthlyFee = scanner.nextDouble();

                preparedStatement.setString(1, id);
                preparedStatement.setString(2, IBAN);
                preparedStatement.setDouble(3, balance);
                preparedStatement.setDate(4, Date.valueOf(dateOpened));
                preparedStatement.setString(5, customer.getId());
                preparedStatement.setDouble(6, overdraftLimit);
                preparedStatement.setDouble(7, monthlyFee);
                preparedStatement.executeUpdate();
            } else if (type.equals("savings")) {
                customerType += "_id";
                String insertAccount = "INSERT INTO savingsaccount(id, IBAN, balance, dateOpened, " + customerType + ", interestRate, minimumBalance, monthlyWithdrawalLimit, monthlyWithdrew) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertAccount);
                System.out.print("Enter interest rate : ");
                double interestRate = scanner.nextDouble();
                System.out.print("Enter minimum balance : ");
                double minimumBalance = scanner.nextDouble();
                System.out.print("Enter monthly withdrawal limit : ");
                double monthlyWithdrawalLimit = scanner.nextDouble();
                double monthlyWithdrew = 0;

                preparedStatement.setString(1, id);
                preparedStatement.setString(2, IBAN);
                preparedStatement.setDouble(3, balance);
                preparedStatement.setDate(4, Date.valueOf(dateOpened));
                preparedStatement.setString(5, customer.getId());
                preparedStatement.setDouble(6, interestRate);
                preparedStatement.setDouble(7, minimumBalance);
                preparedStatement.setDouble(8, monthlyWithdrawalLimit);
                preparedStatement.setDouble(9, monthlyWithdrew);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void getAccounts(){
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        String accountType = getAccountType(scanner);

        try {
            if(accountType.equals("checking")){
                String select = "select * from checkingaccount";
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                ResultSet resultSet = preparedStatement.executeQuery();

                Account account = mapToAccount(resultSet, "checking");
                while (account != null){
                    CheckingAccount checkingAccount = (CheckingAccount) account;
                    System.out.println(checkingAccount.toString());
                    account = mapToAccount(resultSet, "checking");
                }
            }else{
                String select = "select * from savingsaccount";
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                ResultSet resultSet = preparedStatement.executeQuery();

                Account account = mapToAccount(resultSet, "savings");
                while (account != null){
                    SavingsAccount savingsAccount = (SavingsAccount) account;
                    System.out.println(savingsAccount.toString());
                    account = mapToAccount(resultSet, "savings");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteByIBAN(){
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String accountType = getAccountType(scanner);

        Account account = getAccountByIBAN();

        try{
            if(accountType.equals("checking")){
                String select = "delete from checkingaccount where id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, account.getId());
                preparedStatement.executeUpdate();
            }else{
                String select = "delete from savingsaccount where id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, account.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccountByIBAN(){
        Scanner scanner = new Scanner(System.in);
        String accountType = getAccountType(scanner);
        Account account = getAccountByIBAN();

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            if(accountType.equals("checking")){
                String select = "update checkingaccount set overdraftLimit=?, monthlyFee=? where IBAN=?";
                PreparedStatement preparedStatement = connection.prepareStatement(select);

                System.out.print("Enter overdraft limit : ");
                double overdraftLimit = scanner.nextDouble();
                System.out.print("Enter monthly fee : ");
                double monthlyFee = scanner.nextDouble();

                preparedStatement.setDouble(1, overdraftLimit);
                preparedStatement.setDouble(2, monthlyFee);
                preparedStatement.setString(3, account.getIBAN());
                preparedStatement.executeUpdate();
            }else{
                String select = "update savingsaccount set interestRate=?, minimumBalance=?, monthlyWithdrawalLimit=? where IBAN=?";
                PreparedStatement preparedStatement = connection.prepareStatement(select);

                System.out.print("Enter interest rate : ");
                double interestRate = scanner.nextDouble();
                System.out.print("Enter minimum balance : ");
                double minimumBalance = scanner.nextDouble();
                System.out.print("Enter monthly withdrawal limit : ");
                double monthlyWithdrawalLimit = scanner.nextDouble();

                preparedStatement.setDouble(1, interestRate);
                preparedStatement.setDouble(2, minimumBalance);
                preparedStatement.setDouble(3, monthlyWithdrawalLimit);
                preparedStatement.setString(4, account.getIBAN());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccountType(Scanner scanner){
        System.out.print("Enter account type : checking account / savings account ");
        String accountType = scanner.nextLine();
        while(!accountType.equals("checking") && !accountType.equals("savings")){
            System.out.print("Enter account type : checking account / savings account ");
            accountType = scanner.nextLine();
        }
        return accountType;
    }

    private Account mapToAccount(ResultSet resultSet, String type) throws SQLException{
        if(!resultSet.next())
            return null;
        if(type.equals("checking")) {
            return new CheckingAccount(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6));
        }else{
            return new SavingsAccount(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getString(9),
                    resultSet.getString(10),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getDouble(7),
                    resultSet.getDouble(8));
        }
    }

    public Account getAccountByIBAN(){
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        while(true) {
            Account account = null;
            System.out.print("Enter IBAN : ");
            String IBAN = scanner.nextLine();
            String regex = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(IBAN);
            while (!matcher.matches()){
                System.out.print("Enter IBAN or type exit to exit : ");
                IBAN = scanner.nextLine();
                if(IBAN.equals("exit"))
                    return null;
                matcher = pattern.matcher(IBAN);
            }
            try {
                String select = "select * from checkingaccount where IBAN=?";

                PreparedStatement preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, IBAN);
                ResultSet resultSet = preparedStatement.executeQuery();

                account = mapToAccount(resultSet, "checking");
                if (account != null)
                    return account;

                select = "select * from savingsaccount where IBAN=?";

                preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, IBAN);
                resultSet = preparedStatement.executeQuery();

                account = mapToAccount(resultSet, "savings");
                if(account == null){
                    System.out.println("There is no account with this IBAN, type exit to exit, or press enter to try again");
                    IBAN = scanner.nextLine();
                    if(IBAN.equals("exit")){
                        return null;
                    }
                }else return account;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String generateIBAN(){
        StringBuilder sb = new StringBuilder();
        sb.append("RO00");
        Random random = new Random();
        for(int i = 0; i < 20; i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
