package services;

import config.DatabaseConfiguration;
import models.Business;
import models.Customer;
import models.Individual;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerService {
    public void createCustomer(){
        Scanner scanner = new Scanner(System.in);
        String customerType = getCustomerType(scanner);
        String id = UUID.randomUUID().toString();
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            if (customerType.equals("business")) {
                String insertCustomer = "INSERT INTO business(id, name, CUI, companyNr) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertCustomer);

                System.out.print("Enter company name : ");
                String name = scanner.nextLine();
                System.out.print("Enter CUI : ");
                String CUI = scanner.nextLine();
                System.out.print("Enter company nr : ");
                String nr = scanner.nextLine();

                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, CUI);
                preparedStatement.setString(4, nr);
                preparedStatement.executeUpdate();
            } else if (customerType.equals("individual")) {
                String insertCustomer = "INSERT INTO individual(id, firstName, lastName, address, phoneNumber, email) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertCustomer);

                System.out.print("Enter first name : ");
                String firstName = scanner.nextLine();

                System.out.print("Enter last name : ");
                String lastName = scanner.nextLine();

                System.out.print("Enter address : ");
                String address = scanner.nextLine();

                System.out.print("Enter phone number : ");
                String phoneNumber = scanner.nextLine();
                String regex = "^\\d{10}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(phoneNumber);
                while (!matcher.matches()){
                    System.out.print("Enter phone number : ");
                    phoneNumber = scanner.nextLine();
                    matcher = pattern.matcher(phoneNumber);
                }

                System.out.print("Enter email : ");
                String email = scanner.nextLine();
                regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(email);
                while (!matcher.matches()){
                    System.out.print("Enter email : ");
                    email = scanner.nextLine();
                    matcher = pattern.matcher(email);
                }
                System.out.println();

                preparedStatement.setString(1, id);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setString(6, email);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Customer getCustomerByName(String customerType){
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String name = scanner.nextLine();

        while(true){
            Customer customer = null;
            try {
                if (customerType.equals("business")) {
                    String select = "select * from business where name=?";
    
                    PreparedStatement preparedStatement = connection.prepareStatement(select);
                    preparedStatement.setString(1, name);
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    customer = mapToCustomer(resultSet, "business");
                    if(customer == null){
                        System.out.println("There is no business with this name, type exit to exit");
                        name = scanner.nextLine();
                        if(name.equals("exit")){
                            return null;
                        }
                    }else return customer;
                } else {
                    String[] names = name.split(" ", 2);
                    while(names.length != 2){
                        System.out.println("Enter the first and last name of the customer");
                        name = scanner.nextLine();
                        names = name.split(" ", 2);
                    }

                    String select = "select * from individual where firstName=? and lastName=?";
    
                    PreparedStatement preparedStatement = connection.prepareStatement(select);
                    preparedStatement.setString(1, names[0]);
                    preparedStatement.setString(2, names[1]);
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    customer = mapToCustomer(resultSet, "individual");
                    if(customer == null){
                        System.out.println("There is no customer with this first and last name, type exit to exit");
                        name = scanner.nextLine();
                        if(name.equals("exit")){
                            return null;
                        }
                    }else return customer;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void getCustomers(){
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        String customerType = getCustomerType(scanner);
        try {
            if(customerType.equals("business")){
                String select = "select * from business";
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                ResultSet resultSet = preparedStatement.executeQuery();

                Customer customer = mapToCustomer(resultSet, "business");
                while (customer != null){
                    Business business = (Business) customer;
                    System.out.println(business.toString());
                    customer = mapToCustomer(resultSet, "business");
                }
            }else{
                String select = "select * from individual";
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                ResultSet resultSet = preparedStatement.executeQuery();

                Customer customer = mapToCustomer(resultSet, "individual");
                while(customer != null){
                    Individual individual = (Individual) customer;
                    System.out.println(individual.toString());
                    customer = mapToCustomer(resultSet, "individual");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateCustomer(){
        Scanner scanner = new Scanner(System.in);
        String customerType = getCustomerType(scanner);

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            if (customerType.equals("business")) {
                System.out.println("Enter the name of the business");
                Customer customer = getCustomerByName("business");
                Business business = (Business) customer;

                String insertCustomer = "update business set name=?, CUI=?, companyNr=? where name=?";
                PreparedStatement preparedStatement = connection.prepareStatement(insertCustomer);

                System.out.print("Enter company name : ");
                String newName = scanner.nextLine();
                System.out.print("Enter CUI : ");
                String CUI = scanner.nextLine();
                System.out.print("Enter company nr : ");
                String nr = scanner.nextLine();

                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, CUI);
                preparedStatement.setString(3, nr);
                preparedStatement.setString(4, business.getName());
                preparedStatement.executeUpdate();
            } else {
                System.out.println("Enter the first and last name of the customer");
                Customer customer = getCustomerByName("individual");
                Individual individual = (Individual) customer;

                String insertCustomer = "update individual set firstName=?, lastName=?, address=?, phoneNumber=?, email=? where firstName=? and lastName=?";
                PreparedStatement preparedStatement = connection.prepareStatement(insertCustomer);

                System.out.print("Enter first name : ");
                String firstName = scanner.nextLine();

                System.out.print("Enter last name : ");
                String lastName = scanner.nextLine();

                System.out.print("Enter address : ");
                String address = scanner.nextLine();

                System.out.print("Enter phone number : ");
                String phoneNumber = scanner.nextLine();
                String regex = "^\\d{10}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(phoneNumber);
                while (!matcher.matches()){
                    System.out.print("Enter phone number : ");
                    phoneNumber = scanner.nextLine();
                    matcher = pattern.matcher(phoneNumber);
                }

                System.out.print("Enter email : ");
                String email = scanner.nextLine();
                regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(email);
                while (!matcher.matches()){
                    System.out.print("Enter email : ");
                    email = scanner.nextLine();
                    matcher = pattern.matcher(email);
                }
                System.out.println();

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, address);
                preparedStatement.setString(4, phoneNumber);
                preparedStatement.setString(5, email);
                preparedStatement.setString(6, individual.getFirstName());
                preparedStatement.setString(7, individual.getLastName());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteCustomer(){
        Scanner scanner = new Scanner(System.in);
        String customerType = getCustomerType(scanner);

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            if (customerType.equals("business")) {
                System.out.println("Enter the name of the business");
                Customer customer = getCustomerByName("business");
                Business business = (Business) customer;
                String select = "delete from business where name=?";

                PreparedStatement preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, business.getName());
                preparedStatement.executeUpdate();
            } else {
                System.out.println("Enter the first and last name of the customer");
                Customer customer = getCustomerByName("individual");
                Individual individual = (Individual) customer;

                String select = "delete from individual where firstName=? and lastName=?";

                PreparedStatement preparedStatement = connection.prepareStatement(select);
                preparedStatement.setString(1, individual.getFirstName());
                preparedStatement.setString(2, individual.getLastName());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Customer mapToCustomer(ResultSet resultSet, String type) throws SQLException{
        if(!resultSet.next())
            return null;
        if(type.equals("individual")) {
            return new Individual(resultSet.getString(1),
                    null,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6));
        }else{
            return new Business(resultSet.getString(1),
                    null,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4));
        }
    }

    public String getCustomerType(Scanner scanner){
        System.out.print("Select customer type : individual / business ");
        String customerType = scanner.nextLine();
        while(!customerType.equals("business") && !customerType.equals("individual")){
            System.out.print("Select customer type : individual / business ");
            customerType = scanner.nextLine();
        }
        return customerType;
    }

}
