import config.DatabaseConfiguration;
import models.Account;
import models.Card;
import models.Customer;
import models.Statement;
import services.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static ArrayList<Card> cards = new ArrayList<>();
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Statement> statements = new ArrayList<>();
    private static final CustomerService customerService = new CustomerService();
    private static final AccountService accountService = new AccountService();
    private static final StatementService statementService = new StatementService();
    private static final CardService cardService = new CardService();
    private static final TransactionService transactionService = new TransactionService();
    private static ArrayList<String> commands = new ArrayList<>();

    public static void printCommands(){
        for(String command : commands){
            System.out.println(command);
        }
    }

    public static void main(String[] args) throws IOException {
        AuditService auditService = new AuditService();

        commands.add("1. Create customer");
        commands.add("2. Print all customers");
        commands.add("3. Delete customer");
        commands.add("4. Update customer");
        commands.add("5. Create account");
        commands.add("6. Get all accounts");
        commands.add("7. Delete account");
        commands.add("8. Update account");
        commands.add("9. Transfer money");
        commands.add("10. Create statement");
        commands.add("11. Get all statements");
        commands.add("12. Create card");
        commands.add("13. Get all cards");
        commands.add("14. Delete card");
        commands.add("0. Exit");
        boolean end = true;
        Scanner scanner = new Scanner(System.in);
        while(end){
            printCommands();
            int option = scanner.nextInt();
            switch (option){
                case 1 -> {
                    customerService.createCustomer();
                    auditService.write("Customer create");
                }
                case 2 -> {
                    customerService.getCustomers();
                    auditService.write("Get customers");
                }
                case 3 -> {
                    customerService.deleteCustomer();
                    auditService.write("Customer delete");
                }
                case 4 -> {
                    customerService.updateCustomer();
                    auditService.write("Customer update");
                }
                case 5 -> {
                    accountService.createAccount();
                    auditService.write("Account create");
                }
                case 6 -> {
                    accountService.getAccounts();
                    auditService.write("Get accounts");
                }
                case 7 -> {
                    accountService.deleteByIBAN();
                    auditService.write("Account delete");
                }
                case 8 -> {
                    accountService.updateAccountByIBAN();
                    auditService.write("Account update");
                }
                case 9 -> {
                    transactionService.transferMoney();
                    auditService.write("Money transfer");
                }
                case 10 -> {
                    statementService.createStatement();
                    auditService.write("Statement create");
                }
                case 11 -> {
                    statementService.getStatements();
                    auditService.write("Get statements");
                }
                case 12 -> {
                    cardService.createCard();
                    auditService.write("Card create");
                }
                case 13 -> {
                    cardService.getCards();
                    auditService.write("Get cards");
                }
                case 14 -> {
                    cardService.deleteCard();
                    auditService.write("Card delete");
                }
                default -> end = false;
            }
        }
    }
}
